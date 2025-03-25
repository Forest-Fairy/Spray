package top.spray.engine.process.processor.builder.programing;

import top.spray.engine.process.infrastructure.execute.SprayRunnable;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

import java.util.*;

/**
 * a programing workflow for spray
 */
public abstract class SprayProgramingWorkflow<
        WorkflowMeta extends SprayProcessCoordinatorMeta,
        Step extends SprayProcessExecuteStepMeta>
implements SprayRunnable<SprayCoordinatorStatus> {
    protected final WorkflowMeta workflowMeta;
    protected final List<StepDescriptor<Step>> roads;
    private volatile boolean sorted;
    public SprayProgramingWorkflow(WorkflowMeta workflowMeta) {
        this.workflowMeta = workflowMeta;
        this.roads = new LinkedList<>();
        this.sorted = false;
    }
    public synchronized SprayProcessCoordinator start(String transactionId) {
        if (! this.sorted) {
            sort();
            this.sorted = true;
        }
        return startInternal(transactionId);
    }

    protected abstract SprayProcessCoordinator startInternal(String transactionId);
    protected abstract void setNextNodesForStep(Step step, List<Step> nextNodes);
    protected abstract void receiveSortedStartNodes(List<Step> startNodes);

    /**
     *      C _____
     *     /       \
     * A <          \
     *     \         \
     *      D         > F
     *       \       /
     *        > E --
     *      /
     * B --
     */
    protected void sort() {
        int initialCapacity = this.roads.stream().mapToInt(road -> road.getSteps().size()).sum();
        Map<String, StepLinker<Step>> stepMap = new HashMap<>(initialCapacity+1, 1f);
        Set<String> nextSet = new HashSet<>(initialCapacity);
        for (StepDescriptor<Step> road : this.roads) {
            List<Step> roadList = new ArrayList<>(road.getSteps().values());
            for (int i = 0; i < roadList.size(); i++) {
                Step step = roadList.get(i);
                String id = step.getId();
                List<Step> nextNodeList = new LinkedList<>();
                this.setNextNodesForStep(step, nextNodeList);
                stepMap.computeIfAbsent(id,
                        k -> new StepLinker<>(step, nextNodeList));
                if (i + 1 < roadList.size()) {
                    Step nextStep = roadList.get(i + 1);
                    String nextId = nextStep.getId();
                    nextSet.add(nextId);
                    stepMap.get(id).add(nextStep);
                }
            }
        }
        Set<String> startNodeIds = new HashSet<>(stepMap.keySet());
        startNodeIds.removeAll(nextSet);
        List<Step> startNodes = stepMap.entrySet().stream()
                .filter(entry -> startNodeIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(StepLinker::getStep)
                .toList();
        this.receiveSortedStartNodes(startNodes);
    }




    /**
     *      C _____
     *     /       \
     * A <          \
     *     \         \
     *      D         > F
     *       \       /
     *        > E --
     * B ----/
     *
     * method for add a road
     *  each road may contain multiple steps, and they will be sorted before execution
     */
    /** each road in workflow eg. a->c->f, a->d->e, b->e, e->f */
    @SafeVarargs
    public final SprayProgramingWorkflow<WorkflowMeta, Step> addRoad(Step... steps) {
        this.roads.add(new StepDescriptor<>(steps));
        return this;
    }

    static class StepLinker<Step extends SprayProcessExecuteStepMeta> {
        final Step step;
        final List<Step> nextSteps;
        StepLinker(Step step, List<Step> nextSteps) {
            this.step = step;
            this.nextSteps = nextSteps;
        }
        Step getStep() {
            return this.step;
        }
        void add(Step step) {
            this.nextSteps.add(step);
        }
    }

    static class StepDescriptor<Step extends SprayProcessExecuteStepMeta> {
        /** each road in workflow eg. a->c->f, a->d->e, b->e, e->f */
        final Map<String, Step> steps;
        @SafeVarargs
        StepDescriptor(Step... steps) {
            this.steps = new LinkedHashMap<>(steps.length + steps.length / 2);
            if (steps != null) {
                for (Step step : steps) {
                    this.steps.put(step.getId(), step);
                }
            }
        }

        StepDescriptor<Step> add(Step step) {
            Objects.requireNonNull(step, "step can not be null");
            this.steps.put(step.getId(), step);
            return this;
        }
        Map<String, Step> getSteps() {
            return this.steps;
        }
    }

}
