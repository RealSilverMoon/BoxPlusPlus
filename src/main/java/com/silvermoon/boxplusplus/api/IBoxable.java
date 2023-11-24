package com.silvermoon.boxplusplus.api;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Recipe;

/**
 * Implements this to allow your machine encapsulating in Box System.
 * If you don't have a standard getRecipeMap(), override getRealRecipeMap().
 */
public interface IBoxable {

    /**
     * Set which module your machine will be.
     *
     * @return Module ID, refers to the meta of the module core block. Range: 0-11
     */

    int getModuleID();

    /**
     * Should use update module
     *
     * @return true - updated
     */
    boolean isUpdateModule();

    /**
     * Override this if your machine has different mode.
     *
     * @param machine normally "this"
     * @return GT_Recipe_Map - the real recipeMap
     */
    default GT_Recipe.GT_Recipe_Map getRealRecipeMap(GT_MetaTileEntity_MultiBlockBase machine) {
        return machine.getRecipeMap();
    }

    default int getModuleIDSafely() {
        int moduleId = getModuleID();
        if (moduleId < 0 || moduleId > 11) {
            throw new IllegalArgumentException("Module ID must be between 0 and 11.");
        }
        return moduleId;
    }
}
