package top.spray.core;

import java.util.LinkedList;
import java.util.List;

/**
 * a programing workflow for spray
 */
public class SprayProgramingWorkflow<WorkflowMeta, WorkflowStatusInstance, Step> {
    public static <WorkflowMeta, WorkflowStatusInstance> SprayProgramingWorkflow<WorkflowMeta, WorkflowStatusInstance> create(WorkflowMeta workflowMeta) {

    }

    private WorkflowMeta workflowMeta;
    private List<StepDescriptor<Step>> steps;
    public SprayProgramingWorkflow(WorkflowMeta workflowMeta) {
        this.workflowMeta = workflowMeta;
        this.steps = new LinkedList<>();
    }
    public WorkflowStatusInstance start() {

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
     */
    public SprayProgramingWorkflow<WorkflowMeta, WorkflowStatusInstance> addHierarchySteps(Step... steps) {

    }


    /**
     * step1
     * step2 => computeStep
     * step3
     */
    public SprayProgramingWorkflow<WorkflowMeta, WorkflowStatusInstance> compute(Step step) {

    }


    public static class StepDescriptor<Step> {
        private final Step computeStep;
        private final Step[] step;
        StepDescriptor(Step computeStep, Step... step) {
            this.computeStep = computeStep;
            this.step = step;
        }
    }

}
