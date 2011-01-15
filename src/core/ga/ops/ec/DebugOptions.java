package core.ga.ops.ec;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 *
 * @author gmatoga
 */
public class DebugOptions {

    private boolean elitistSelectionSpecificOutput = false;
    private boolean generationStatisticsGathered = false;
    private boolean indexMappingOutput = false;
    private boolean indexCreationOutput = false;
    private boolean ruleSavingOutput = false;
    private boolean mutationRuleSavingOutput = false;
    private boolean addingToIndexMapOutput = false;
    private boolean updatingIndexesOutput = false;
    private boolean evolutionPhaseOutput = false;
    private boolean savedRuleOutput = false;
    private boolean selectionResultOutput = false;
    private boolean rulePopulationAfterSelectionOutput = false;

    public boolean isSavedRuleOutput() {
        return savedRuleOutput;
    }

    public void setSavedRuleOutput(boolean savedRuleOutput) {
        this.savedRuleOutput = savedRuleOutput;
    }

    public boolean isEvolutionPhaseOutput() {
        return evolutionPhaseOutput;
    }

    public void setEvolutionPhaseOutput(boolean evolutionPhaseOutput) {
        this.evolutionPhaseOutput = evolutionPhaseOutput;
    }

    public boolean isUpdatingIndexesOutput() {
        return updatingIndexesOutput;
    }

    public void setUpdatingIndexesOutput(boolean updatingIndexesOutput) {
        this.updatingIndexesOutput = updatingIndexesOutput;
    }

    public boolean isMutationRuleSavingOutput() {
        return mutationRuleSavingOutput;
    }

    public void setMutationRuleSavingOutput(boolean mutationRuleSavingOutput) {
        this.mutationRuleSavingOutput = mutationRuleSavingOutput;
    }

    public boolean isElitistSelectionSpecificOutput() {
        return elitistSelectionSpecificOutput;
    }

    public void setElitistSelectionSpecificOutput(boolean elitistSelectionSpecificOutput) {
        this.elitistSelectionSpecificOutput = elitistSelectionSpecificOutput;
    }

    public boolean isGenerationStatisticsOutput() {
        return generationStatisticsGathered;
    }

    public void setGenerationStatisticsGathered(boolean generationStatisticsGathered) {
        this.generationStatisticsGathered = generationStatisticsGathered;
    }

    public boolean isIndexMappingOutput() {
        return indexMappingOutput;
    }

    public void setIndexMappingOutput(boolean indexMappingOutput) {
        this.indexMappingOutput = indexMappingOutput;
    }

    public boolean isIndexCreationOutput() {
        return indexCreationOutput;
    }

    public void setIndexCreationOutput(boolean indexCreationOutput) {
        this.indexCreationOutput = indexCreationOutput;
    }

    public boolean isRuleSavingOutput() {
        return ruleSavingOutput;
    }

    public void setRuleSavingOutput(boolean ruleSavingOutput) {
        this.ruleSavingOutput = ruleSavingOutput;
    }

    public boolean isAddingToIndexMapOutput() {
        return addingToIndexMapOutput;
    }

    public void setAddingToIndexMapOutput(boolean addingToIndexMapOutput) {
        this.addingToIndexMapOutput = addingToIndexMapOutput;
    }

    public void setAllTrue() {
        try {
            BeanInfo info = Introspector.getBeanInfo(this.getClass());
            for (PropertyDescriptor desc : info.getPropertyDescriptors()) {
                Method writeMethod = desc.getWriteMethod();
                if (writeMethod != null)
                    writeMethod.invoke(this, Boolean.TRUE);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isSelectionResultOutput() {
        return selectionResultOutput;
    }

    public void setSelectionResultOutput(boolean selectionResultOutput) {
        this.selectionResultOutput = selectionResultOutput;
    }

    public boolean isRulePopulationAfterSelectionOutput() {
        return rulePopulationAfterSelectionOutput;
    }

    public void setRulePopulationAfterSelectionOutput(
            boolean rulePopulationAfterSelectionOutput) {
        this.rulePopulationAfterSelectionOutput =
                rulePopulationAfterSelectionOutput;
    }
}
