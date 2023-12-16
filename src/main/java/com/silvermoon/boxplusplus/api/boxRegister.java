package com.silvermoon.boxplusplus.api;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.*;
import com.silvermoon.boxplusplus.boxplusplus;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

public class boxRegister {

    public static final List<GT_MetaTileEntity_MultiBlockBase> customerMachineList = new ArrayList<>();
    public static final Multimap<Integer, String> customModuleList = HashMultimap.create();
    public static final Multimap<Integer, String> customUpdatedModuleList = HashMultimap.create();

    /**
     * Use this to register your boxable machine. **Should be called on CommonProxy.postInit().**
     * <p>
     * 在CommonProxy.postInit()阶段调用本方法。确保传入的GT_MetaTileEntity_MultiBlockBase已经实现了IBoxable。
     *
     * @param machineList You can put multi-GTMultiMachines in this parameter.
     *                    <p>
     *                    可以接受多个参数
     */
    public static void registerMachineToBox(GT_MetaTileEntity_MultiBlockBase... machineList) {
        for (GT_MetaTileEntity_MultiBlockBase machine : machineList) {
            if (machine instanceof IBoxable boxable) {
                customerMachineList.add(machine);
                if (boxable.isUpdateModule()) {
                    customUpdatedModuleList.put(boxable.getModuleIDSafely(), "| " + machine.getLocalName());
                } else customModuleList.put(boxable.getModuleIDSafely(), "| " + machine.getLocalName());
            } else
                boxplusplus.LOG.error("Trying to add a unboxable MultiBlock to Box! Trace: " + machine.getLocalName());
        }
    }
}
