package com.silvermoon.boxplusplus.common.tileentities;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizons.modularui.api.math.Alignment.TopCenter;
import static com.gtnewhorizons.modularui.api.math.Alignment.TopLeft;
import static com.silvermoon.boxplusplus.common.BoxModule.getModuleByIndex;
import static com.silvermoon.boxplusplus.common.BoxModule.transMachinesToModule;
import static com.silvermoon.boxplusplus.util.Util.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.io.IOException;
import java.util.*;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.jetbrains.annotations.NotNull;

import com.glodblock.github.loader.ItemAndBlockHolder;
import com.glodblock.github.util.FluidPatternDetails;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.KeyboardUtil;
import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.*;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;
import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.common.loader.BlockRegister;
import com.silvermoon.boxplusplus.util.*;

import appeng.api.AEApi;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.misc.WirelessNetworkManager;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public class GTMachineBox extends MTEExtendedPowerMultiBlockBase<GTMachineBox> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MainFrames = "Mainframes";
    private static final String STRUCTURE_PIECE_FirstRing = "FirstRing";
    private static final String STRUCTURE_PIECE_SecondRing = "SecondRing";
    private static final String STRUCTURE_PIECE_Final = "Final";
    private static final IIconContainer boxActive = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER_ACTIVE");
    private static final IIconContainer boxInactive = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER");
    private int extendCasing = 0;
    private final boolean[] moduleSwitch = new boolean[15];
    private boolean[] moduleActive = new boolean[15];
    private final int[] moduleTier = new int[15];
    public final ArrayList<BoxRoutings> routingMap = new ArrayList<>();
    public int moduleSN = 1;
    public List<Integer> randomSN = new ArrayList<>(Arrays.asList(1));
    public int ringCount = 1;
    public int wikiPageCode = 1;
    public int routingPageCode = 1;
    public int ringCountSet = 1;
    public int routingStatus = 0;
    private int[] machineError = new int[2];
    private int maxParallel = 160;
    private int maxRouting = 16;
    // What's that?
    private static final char[] coreElement = { 'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M' };
    public BoxRecipe recipe = new BoxRecipe();
    protected TEBoxRing teBoxRing;
    public UUID userUUID;
    public boolean debug = false;
    public static IStructureDefinition<GTMachineBox> STRUCTURE_DEFINITION;

    // The spotless made my structure a mess. Shit.

    // spotless off
    static {
        StructureDefinition.Builder<GTMachineBox> A = IStructureDefinition.<GTMachineBox>builder()
            .addShape(
                STRUCTURE_PIECE_MainFrames,
                transpose(
                    new String[][] {
                        { "       ", "   C   ", "  CCC  ", " CCCCC ", "  CCC  ", "   C   ", "       ", "       ",
                            "       ", "       ", "       " },
                        { "   C   ", " CCCCC ", " CC CC ", "CC   CC", " CC CC ", " CCCCC ", "   C   ", "       ",
                            "       ", "       ", "       " },
                        { "  CCC  ", " CC CC ", "CC   CC", "C     C", "CC   CC", " CC CC ", "  CCC  ", "       ",
                            "       ", "       ", "       " },
                        { " CC~CC ", "CC   CC", "C     C", "C  D  C", "C     C", "CC   CC", " CCCCC ", "   C   ",
                            "   C   ", "   C   ", "   C   " },
                        { "  CCC  ", " CC CC ", "CC   CC", "C     C", "CC   CC", " CC CC ", "  CCC  ", "       ",
                            "       ", "       ", "       " },
                        { "   C   ", " CCCCC ", " CC CC ", "CC   CC", " CC CC ", " CCCCC ", "   C   ", "       ",
                            "       ", "       ", "       " },
                        { "       ", "   C   ", "  CCC  ", " CCCCC ", "  CCC  ", "   C   ", "       ", "       ",
                            "       ", "       ", "       " } }))
            .addShape(
                STRUCTURE_PIECE_FirstRing,
                transpose(
                    new String[][] {
                        { "           E           ", "                       ", "           E           ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "E E                 E E",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "           E           ",
                            "                       ", "           E           " },
                        { "          EEE          ", "                       ", "          EEE          ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "E E                 E E", "E E                 E E",
                            "E E                 E E", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "          EEE          ",
                            "                       ", "          EEE          " },
                        { "         EEEEE         ", "      EEEEEEEEEEE      ", "     EEE EEEEE EEE     ",
                            "    EE     E     EE    ", "   EE      E      EE   ", "  EE               EE  ",
                            " EE                 EE ", " EE                 EE ", " E                   E ",
                            "EEE                 EEE", "EEE                 EEE", "EEEEE             EEEEE",
                            "EEE                 EEE", "EEE                 EEE", " E                   E ",
                            " EE                 EE ", " EE                 EE ", "  EE               EE  ",
                            "   EE      E      EE   ", "    EE     E     EE    ", "     EEE EEEEE EEE     ",
                            "      EEEEEEEEEEE      ", "         EEEEE         " },
                        { "        EEE EEE        ", "          E E          ", "        EEE EEE        ",
                            "          E E          ", "          E E          ", "                       ",
                            "                       ", "                       ", "E E                 E E",
                            "E E                 E E", "EEEEE             EEEEE", "                       ",
                            "EEEEE             EEEEE", "E E                 E E", "E E                 E E",
                            "                       ", "                       ", "                       ",
                            "          E E          ", "          E E          ", "        EEE EEE        ",
                            "          E E          ", "        EEE EEE        " },
                        { "         EEEEE         ", "      EEEEEEEEEEE      ", "     EEE EEEEE EEE     ",
                            "    EE     E     EE    ", "   EE      E      EE   ", "  EE               EE  ",
                            " EE                 EE ", " EE                 EE ", " E                   E ",
                            "EEE                 EEE", "EEE                 EEE", "EEEEE             EEEEE",
                            "EEE                 EEE", "EEE                 EEE", " E                   E ",
                            " EE                 EE ", " EE                 EE ", "  EE               EE  ",
                            "   EE      E      EE   ", "    EE     E     EE    ", "     EEE EEEEE EEE     ",
                            "      EEEEEEEEEEE      ", "         EEEEE         " },
                        { "          EEE          ", "                       ", "          EEE          ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "E E                 E E", "E E                 E E",
                            "E E                 E E", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "          EEE          ",
                            "                       ", "          EEE          " },
                        { "           E           ", "                       ", "           E           ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "E E                 E E",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "                       ",
                            "                       ", "                       ", "           E           ",
                            "                       ", "           E           " } }))
            .addShape(
                STRUCTURE_PIECE_SecondRing,
                transpose(
                    new String[][] {
                        { "                 F                 ", "                                   ",
                            "                 F                 ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "F F                             F F",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                 F                 ", "                                   ",
                            "                 F                 " },
                        { "                FFF                ", "                                   ",
                            "                FFF                ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "F F                             F F", "F F                             F F",
                            "F F                             F F", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                FFF                ", "                                   ",
                            "                FFF                " },
                        { "                F F                ", "                                   ",
                            "                F F                ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "F F                             F F", "                                   ",
                            "F F                             F F", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                F F                ", "                                   ",
                            "                F F                " },
                        { "              FFFFFFF              ", "           FFFFFFFFFFFFF           ",
                            "         FFFF FFFFFFF FFFF         ", "        FF       F       FF        ",
                            "      FFF        F        FFF      ", "     FF          F          FF     ",
                            "    FF                       FF    ", "    F                         F    ",
                            "   FF                         FF   ", "  FF                           FF  ",
                            "  F                             F  ", " FF                             FF ",
                            " FF                             FF ", " F                               F ",
                            "FFF                             FFF", "FFF                             FFF",
                            "FFF                             FFF", "FFFFFF                       FFFFFF",
                            "FFF                             FFF", "FFF                             FFF",
                            "FFF                             FFF", " F                               F ",
                            " FF                             FF ", " FF                             FF ",
                            "  F                             F  ", "  FF                           FF  ",
                            "   FF                         FF   ", "    F                         F    ",
                            "    FF                       FF    ", "     FF          F          FF     ",
                            "      FFF        F        FFF      ", "        FF       F       FF        ",
                            "         FFFF FFFFFFF FFFF         ", "           FFFFFFFFFFFFF           ",
                            "              FFFFFFF              " },
                        { "             FF F F FF             ", "                F F                ",
                            "             FF F F FF             ", "                F F                ",
                            "                F F                ", "                F F                ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "F F                             F F",
                            "F F                             F F", "                                   ",
                            "FFFFFF                       FFFFFF", "                                   ",
                            "FFFFFF                       FFFFFF", "                                   ",
                            "F F                             F F", "F F                             F F",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                F F                ",
                            "                F F                ", "                F F                ",
                            "             FF F F FF             ", "                F F                ",
                            "             FF F F FF             " },
                        { "              FFFFFFF              ", "           FFFFFFFFFFFFF           ",
                            "         FFFF FFFFFFF FFFF         ", "        FF       F       FF        ",
                            "      FFF        F        FFF      ", "     FF          F          FF     ",
                            "    FF                       FF    ", "    F                         F    ",
                            "   FF                         FF   ", "  FF                           FF  ",
                            "  F                             F  ", " FF                             FF ",
                            " FF                             FF ", " F                               F ",
                            "FFF                             FFF", "FFF                             FFF",
                            "FFF                             FFF", "FFFFFF                       FFFFFF",
                            "FFF                             FFF", "FFF                             FFF",
                            "FFF                             FFF", " F                               F ",
                            " FF                             FF ", " FF                             FF ",
                            "  F                             F  ", "  FF                           FF  ",
                            "   FF                         FF   ", "    F                         F    ",
                            "    FF                       FF    ", "     FF          F          FF     ",
                            "      FFF        F        FFF      ", "        FF       F       FF        ",
                            "         FFFF FFFFFFF FFFF         ", "           FFFFFFFFFFFFF           ",
                            "              FFFFFFF              " },
                        { "                F F                ", "                                   ",
                            "                F F                ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "F F                             F F", "                                   ",
                            "F F                             F F", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                F F                ", "                                   ",
                            "                F F                " },
                        { "                FFF                ", "                                   ",
                            "                FFF                ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "F F                             F F", "F F                             F F",
                            "F F                             F F", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                FFF                ", "                                   ",
                            "                FFF                " },
                        { "                 F                 ", "                                   ",
                            "                 F                 ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "F F                             F F",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                                   ", "                                   ",
                            "                 F                 ", "                                   ",
                            "                 F                 " } }))
            .addShape(
                STRUCTURE_PIECE_Final,
                transpose(
                    new String[][] {
                        { "                       G                       ",
                            "                                               ",
                            "                       G                       ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                       G                       ",
                            "                                               ",
                            "                       G                       " },
                        { "                      GGG                      ",
                            "                                               ",
                            "                      GGG                      ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                      GGG                      ",
                            "                                               ",
                            "                      GGG                      " },
                        { "                      G G                      ",
                            "                                               ",
                            "                      G G                      ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "                                               ",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                      G G                      ",
                            "                                               ",
                            "                      G G                      " },
                        { "                     GGGGG                     ",
                            "                GGGGGGGGGGGGGGG                ",
                            "              GGGG   GG GG   GGGG              ",
                            "            GGG                 GGG            ",
                            "          GGG                     GGG          ",
                            "         GG                         GG         ",
                            "        GG                           GG        ",
                            "       GG                             GG       ",
                            "      GG                               GG      ",
                            "     GG                                 GG     ",
                            "    GG                                   GG    ",
                            "    G                                     G    ",
                            "   GG                                     GG   ",
                            "   G                                       G   ",
                            "  GG                                       GG  ",
                            "  G                                         G  ",
                            " GG                                         GG ",
                            " GG                                         GG ",
                            " G                                           G ",
                            " G                                           G ",
                            " G                                           G ",
                            "GGG                                         GGG",
                            "GGG                                         GGG",
                            "GG                                           GG",
                            "GGG                                         GGG",
                            "GGG                                         GGG",
                            " G                                           G ",
                            " G                                           G ",
                            " G                                           G ",
                            " GG                                         GG ",
                            " GG                                         GG ",
                            "  G                                         G  ",
                            "  GG                                       GG  ",
                            "   G                                       G   ",
                            "   GG                                     GG   ",
                            "    G                                     G    ",
                            "    GG                                   GG    ",
                            "     GG                                 GG     ",
                            "      GG                               GG      ",
                            "       GG                             GG       ",
                            "        GG                           GG        ",
                            "         GG                         GG         ",
                            "          GGG                     GGG          ",
                            "            GGG                 GGG            ",
                            "              GGGG   GG GG   GGGG              ",
                            "                GGGGGGGGGGGGGGG                ",
                            "                     GGGGG                     " },
                        { "                   GGG G GGG                   ",
                            "                       G                       ",
                            "                   GGG G GGG                   ",
                            "                       G                       ",
                            "                       G                       ",
                            "                       G                       ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "GGGGGG                                   GGGGGG",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                       G                       ",
                            "                       G                       ",
                            "                       G                       ",
                            "                   GGG G GGG                   ",
                            "                       G                       ",
                            "                   GGG G GGG                   " },
                        { "                  GG  G G  GG                  ",
                            "                      G G                      ",
                            "                  GG  G G  GG                  ",
                            "                      G G                      ",
                            "                      G G                      ",
                            "                      G G                      ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "GGGGGG                                   GGGGGG",
                            "                                               ",
                            "GGGGGG                                   GGGGGG",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                      G G                      ",
                            "                      G G                      ",
                            "                      G G                      ",
                            "                  GG  G G  GG                  ",
                            "                      G G                      ",
                            "                  GG  G G  GG                  " },
                        { "                   GGG G GGG                   ",
                            "                       G                       ",
                            "                   GGG G GGG                   ",
                            "                       G                       ",
                            "                       G                       ",
                            "                       G                       ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "GGGGGG                                   GGGGGG",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                       G                       ",
                            "                       G                       ",
                            "                       G                       ",
                            "                   GGG G GGG                   ",
                            "                       G                       ",
                            "                   GGG G GGG                   " },
                        { "                     GGGGG                     ",
                            "                GGGGGGGGGGGGGGG                ",
                            "              GGGG   GG GG   GGGG              ",
                            "            GGG                 GGG            ",
                            "          GGG                     GGG          ",
                            "         GG                         GG         ",
                            "        GG                           GG        ",
                            "       GG                             GG       ",
                            "      GG                               GG      ",
                            "     GG                                 GG     ",
                            "    GG                                   GG    ",
                            "    G                                     G    ",
                            "   GG                                     GG   ",
                            "   G                                       G   ",
                            "  GG                                       GG  ",
                            "  G                                         G  ",
                            " GG                                         GG ",
                            " GG                                         GG ",
                            " G                                           G ",
                            " G                                           G ",
                            " G                                           G ",
                            "GGG                                         GGG",
                            "GGG                                         GGG",
                            "GG                                           GG",
                            "GGG                                         GGG",
                            "GGG                                         GGG",
                            " G                                           G ",
                            " G                                           G ",
                            " G                                           G ",
                            " GG                                         GG ",
                            " GG                                         GG ",
                            "  G                                         G  ",
                            "  GG                                       GG  ",
                            "   G                                       G   ",
                            "   GG                                     GG   ",
                            "    G                                     G    ",
                            "    GG                                   GG    ",
                            "     GG                                 GG     ",
                            "      GG                               GG      ",
                            "       GG                             GG       ",
                            "        GG                           GG        ",
                            "         GG                         GG         ",
                            "          GGG                     GGG          ",
                            "            GGG                 GGG            ",
                            "              GGGG   GG GG   GGGG              ",
                            "                GGGGGGGGGGGGGGG                ",
                            "                     GGGGG                     " },
                        { "                      G G                      ",
                            "                                               ",
                            "                      G G                      ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "                                               ",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                      G G                      ",
                            "                                               ",
                            "                      G G                      " },
                        { "                      GGG                      ",
                            "                                               ",
                            "                      GGG                      ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "G G                                         G G",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                      GGG                      ",
                            "                                               ",
                            "                      GGG                      " },
                        { "                       G                       ",
                            "                                               ",
                            "                       G                       ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "G G                                         G G",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                                               ",
                            "                       G                       ",
                            "                                               ",
                            "                       G                       " } }));
        for (int i = 0; i < 14; i++) {
            A.addShape(getModuleByIndex(i).name, transpose(getModuleByIndex(i).moduleStructure));
        }
        A.addElement(
            'C',
            buildHatchAdder(GTMachineBox.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Energy, ExoticEnergy, Maintenance)
                .casingIndex(114 << 7)
                .dot(1)
                .buildAndChain(onElementPass(i -> ++i.extendCasing, ofBlock(BlockRegister.SpaceExtend, 0))))
            .addElement('D', Util.RingTileAdder((v, t) -> {
                if ((t.getBlockType()
                    .isAssociatedBlock(BlockRegister.BoxRing) && v.ringCountSet != 1)) return false;
                if ((t.getBlockType()
                    .isAssociatedBlock(BlockRegister.BoxRing2) && v.ringCountSet != 2)) return false;
                if ((t.getBlockType()
                    .isAssociatedBlock(BlockRegister.BoxRing3) && v.ringCountSet != 3)) return false;
                v.teBoxRing = t;
                return true;
            },
                TEBoxRing.class,
                BlockRegister.BoxRing,
                0,
                v -> v.ringCountSet == 1 ? BlockRegister.BoxRing
                    : (v.ringCountSet == 2 ? BlockRegister.BoxRing2 : BlockRegister.BoxRing3)))
            .addElement('E', ofBlock(BlockRegister.SpaceCompress, 0))
            .addElement('F', ofBlock(BlockRegister.SpaceConstraint, 0))
            .addElement('G', ofBlock(BlockRegister.SpaceWall, 0));
        for (int i = 0; i < 14; i++) {
            int finalI = i;
            A.addElement(coreElement[i], ofChain(ofBlockAdder((t, b, m) -> {
                if (b.isAssociatedBlock(BlockRegister.BoxModule) && m == finalI) {
                    t.moduleTier[finalI] = 0;
                    if (finalI == 13) t.maxParallel = 1280000;
                    return true;
                }
                return false;
            }, BlockRegister.BoxModule, i), ofBlockAdder((t, b, m) -> {
                if (m == 14 && finalI == 13 && b.isAssociatedBlock(BlockRegister.BoxModuleUpgrad)) {
                    t.debug = true;
                    t.maxParallel = Integer.MAX_VALUE;
                    t.maxRouting = Integer.MAX_VALUE;
                    return true;
                }
                if (b.isAssociatedBlock(BlockRegister.BoxModuleUpgrad) && m == finalI) {
                    t.moduleTier[finalI] = 1;
                    if (m == 13) {
                        t.maxParallel = 99900000;
                        t.maxRouting = 999;
                    }
                    return true;
                }
                t.moduleTier[finalI] = 0;
                return false;
            }, BlockRegister.BoxModuleUpgrad, i)));
        }
        STRUCTURE_DEFINITION = A.build();
    }

    // spotless on

    public GTMachineBox(String name) {
        super(name);
    }

    public GTMachineBox(int ID, String Name, String NameRegional) {
        super(ID, Name, NameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTMachineBox(super.mName);
    }

    @Override
    public IStructureDefinition<GTMachineBox> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(i18n("tile.boxplusplus.boxtype"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.02"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.03"))
            .addSeparator()
            .addInfo(EnumChatFormatting.DARK_GREEN + i18n("tile.boxplusplus.boxinfo.04"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.05"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.06"))
            .addInfo(EnumChatFormatting.GOLD + i18n("tile.boxplusplus.boxinfo.07"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.08"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.09"))
            .addSeparator()
            .addInfo(i18n("tile.boxplusplus.boxinfo.10"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.11"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.12"))
            .addSeparator()
            .addInfo(i18n("tile.boxplusplus.boxinfo.13"))
            .addInfo(i18n("tile.boxplusplus.boxinfo.14"))
            .addInfo(EnumChatFormatting.AQUA + i18n("tile.boxplusplus.boxinfo.15"))
            .addPollutionAmount(0)
            .addSeparator()
            .beginStructureBlock(47, 11, 47, false)
            .addStructureInfo(i18n("tile.boxplusplus.boxStructure.01"))
            .addController(i18n("tile.boxplusplus.boxStructure.02"))
            .addCasingInfoMin(i18n("tile.boxplusplus_SpaceExtend.name"), 130, false)
            .addCasingInfoExactly(i18n("tile.boxplusplus_SpaceCompress.name"), 408, false)
            .addCasingInfoExactly(i18n("tile.boxplusplus_SpaceConstraint.name"), 584, false)
            .addCasingInfoExactly(i18n("tile.boxplusplus_SpaceWall.name"), 760, false)
            .addStructureInfo(i18n("tile.boxplusplus.boxStructure.03"))
            .addEnergyHatch(i18n("tile.boxplusplus.boxStructure.04"))
            .addStructureInfo(EnumChatFormatting.YELLOW + i18n("tile.boxplusplus.boxStructure.05"))
            .addSeparator()
            .toolTipFinisher("BoxPlusPlus");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        switch (ringCountSet) {
            case 1 -> {
                switch (stackSize.stackSize) {
                    case 1 -> {
                        buildPiece(STRUCTURE_PIECE_MainFrames, stackSize, hintsOnly, 3, 3, 0);
                        buildPiece(STRUCTURE_PIECE_FirstRing, stackSize, hintsOnly, 11, 3, 8);
                    }
                    case 2 -> {
                        buildPiece(STRUCTURE_PIECE_MainFrames, stackSize, hintsOnly, 3, 3, 0);
                        buildPiece(STRUCTURE_PIECE_FirstRing, stackSize, hintsOnly, 11, 3, 8);
                        buildPiece(STRUCTURE_PIECE_SecondRing, stackSize, hintsOnly, 17, 5, 14);
                    }
                    default -> {
                        buildPiece(STRUCTURE_PIECE_MainFrames, stackSize, hintsOnly, 3, 3, 0);
                        buildPiece(STRUCTURE_PIECE_FirstRing, stackSize, hintsOnly, 11, 3, 8);
                        buildPiece(STRUCTURE_PIECE_SecondRing, stackSize, hintsOnly, 17, 5, 14);
                        buildPiece(STRUCTURE_PIECE_Final, stackSize, hintsOnly, 23, 5, 20);
                    }
                }
            }
            case 2 -> {
                buildPiece(STRUCTURE_PIECE_MainFrames, stackSize, hintsOnly, 3, 3, 0);
                buildPiece(STRUCTURE_PIECE_FirstRing, stackSize, hintsOnly, 11, 3, 8);
                buildPiece(STRUCTURE_PIECE_SecondRing, stackSize, hintsOnly, 17, 5, 14);
            }
            case 3 -> {
                buildPiece(STRUCTURE_PIECE_MainFrames, stackSize, hintsOnly, 3, 3, 0);
                buildPiece(STRUCTURE_PIECE_FirstRing, stackSize, hintsOnly, 11, 3, 8);
                buildPiece(STRUCTURE_PIECE_SecondRing, stackSize, hintsOnly, 17, 5, 14);
                buildPiece(STRUCTURE_PIECE_Final, stackSize, hintsOnly, 23, 5, 20);
            }
        }
        for (int i = 0; i < 14; i++) {
            if (moduleSwitch[i] || stackSize.stackSize - 4 >= i) {
                buildPiece(
                    getModuleByIndex(i).name,
                    stackSize,
                    hintsOnly,
                    getModuleByIndex(i).horizontalOffset,
                    getModuleByIndex(i).verticalOffset,
                    getModuleByIndex(i).depthOffset);
            }
        }
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        elementBudget = Math.min(200, elementBudget * 4);
        int count = 0;
        switch (ringCountSet) {
            case 1 -> {
                switch (stack.stackSize) {
                    case 1 -> {
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_MainFrames,
                            stack,
                            3,
                            3,
                            0,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (count > -1) {
                            break;
                        }
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_FirstRing,
                            stack,
                            11,
                            3,
                            8,
                            elementBudget,
                            env,
                            false,
                            true);

                    }
                    case 2 -> {
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_MainFrames,
                            stack,
                            3,
                            3,
                            0,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (count > -1) {
                            break;
                        }
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_FirstRing,
                            stack,
                            11,
                            3,
                            8,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (count > -1) {
                            break;
                        }
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_SecondRing,
                            stack,
                            17,
                            5,
                            14,
                            elementBudget,
                            env,
                            false,
                            true);
                    }
                    default -> {
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_MainFrames,
                            stack,
                            3,
                            3,
                            0,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (count > -1) {
                            break;
                        }
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_FirstRing,
                            stack,
                            11,
                            3,
                            8,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (count > -1) {
                            break;
                        }
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_SecondRing,
                            stack,
                            17,
                            5,
                            14,
                            elementBudget,
                            env,
                            false,
                            true);
                        if (count > -1) {
                            break;
                        }
                        count = survivalBuildPiece(
                            STRUCTURE_PIECE_Final,
                            stack,
                            23,
                            5,
                            20,
                            elementBudget,
                            env,
                            false,
                            true);
                    }
                }
            }
            case 2 -> {
                count = survivalBuildPiece(STRUCTURE_PIECE_MainFrames, stack, 3, 3, 0, elementBudget, env, false, true);
                if (count > -1) {
                    break;
                }
                count = survivalBuildPiece(STRUCTURE_PIECE_FirstRing, stack, 11, 3, 8, elementBudget, env, false, true);
                if (count > -1) {
                    break;
                }
                count = survivalBuildPiece(
                    STRUCTURE_PIECE_SecondRing,
                    stack,
                    17,
                    5,
                    14,
                    elementBudget,
                    env,
                    false,
                    true);
            }
            case 3 -> {
                count = survivalBuildPiece(STRUCTURE_PIECE_MainFrames, stack, 3, 3, 0, elementBudget, env, false, true);
                if (count > -1) {
                    break;
                }
                count = survivalBuildPiece(STRUCTURE_PIECE_FirstRing, stack, 11, 3, 8, elementBudget, env, false, true);
                if (count > -1) {
                    break;
                }
                count = survivalBuildPiece(
                    STRUCTURE_PIECE_SecondRing,
                    stack,
                    17,
                    5,
                    14,
                    elementBudget,
                    env,
                    false,
                    true);
                if (count > -1) {
                    break;
                }
                count = survivalBuildPiece(STRUCTURE_PIECE_Final, stack, 23, 5, 20, elementBudget, env, false, true);
            }
        }
        return count;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        ringCount = 1;
        debug = false;
        moduleActive = new boolean[moduleActive.length];
        machineError = new int[2];
        switch (ringCountSet) {
            case 1 -> {
                if (checkPiece(STRUCTURE_PIECE_MainFrames, 3, 3, 0)
                    && checkPiece(STRUCTURE_PIECE_FirstRing, 11, 3, 8)) {
                    ringCount = 1;
                    break;
                }
                if (teBoxRing != null) {
                    teBoxRing.renderStatus = false;
                    teBoxRing = null;
                }
                machineError[0] = 1;
                return false;
            }
            case 2 -> {
                if (checkPiece(STRUCTURE_PIECE_MainFrames, 3, 3, 0) && checkPiece(STRUCTURE_PIECE_FirstRing, 11, 3, 8)
                    && checkPiece(STRUCTURE_PIECE_SecondRing, 17, 5, 14)) {
                    ringCount = 2;
                    maxParallel = 6400;
                    maxRouting = 64;
                    break;
                }
                if (teBoxRing != null) {
                    teBoxRing.renderStatus = false;
                    teBoxRing = null;
                }
                machineError[0] = 2;
                return false;
            }
            case 3 -> {
                if (checkPiece(STRUCTURE_PIECE_MainFrames, 3, 3, 0) && checkPiece(STRUCTURE_PIECE_FirstRing, 11, 3, 8)
                    && checkPiece(STRUCTURE_PIECE_SecondRing, 17, 5, 14)
                    && checkPiece(STRUCTURE_PIECE_Final, 23, 5, 20)) {
                    ringCount = 3;
                    maxParallel = 128000;
                    maxRouting = 128;
                    break;
                }
                if (teBoxRing != null) {
                    teBoxRing.renderStatus = false;
                    teBoxRing = null;
                }
                machineError[0] = 3;
                return false;
            }
            default -> {
                return false;
            }
        }
        for (int i = 0; i < 15; i++) {
            if (moduleSwitch[i]) {
                if (checkPiece(
                    getModuleByIndex(i).name,
                    getModuleByIndex(i).horizontalOffset,
                    getModuleByIndex(i).verticalOffset,
                    getModuleByIndex(i).depthOffset)) {
                    moduleActive[i] = true;
                    continue;
                }
                teBoxRing.renderStatus = false;
                machineError[0] = 4;
                machineError[1] = i + 1;
                return false;
            }
        }
        // If you want it, then you'll have to take it.
        for (MTEHatch hatch : getExoticEnergyHatches()) {
            if (hatch instanceof MTEHatchEnergyMulti && ringCount == 1) {
                machineError[0] = 5;
                return false;
            }
            if (hatch instanceof MTEHatchEnergyTunnel && !moduleActive[12]) {
                machineError[0] = 6;
                return false;
            }
        }
        if (teBoxRing != null && ringCount == 3) teBoxRing.renderStatus = true;
        return true;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mDualInputHatches.clear();
    }

    /**
     * Check if the recipe is the final one.
     *
     * @return CheckRecipeResultRegistry
     */
    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        lEUt = 0;
        mMaxProgresstime = 0;
        mOutputItems = null;
        mOutputFluids = null;
        if (!recipe.islocked) return CheckRecipeResultRegistry.NO_RECIPE;
        List<ItemStack> inputItem = getStoredInputs();
        List<FluidStack> inputFluid = getStoredFluids();
        for (IDualInputHatch hatch : mDualInputHatches) {
            Iterator<? extends IDualInputInventory> meHatchIter = hatch.inventories();
            while (meHatchIter.hasNext()) {
                IDualInputInventory inv = meHatchIter.next();
                inputItem.addAll(Arrays.asList(inv.getItemInputs()));
                inputFluid.addAll(Arrays.asList(inv.getFluidInputs()));
            }
        }
        if ((inputItem.isEmpty() && !recipe.FinalItemInput.isEmpty())
            || (inputFluid.isEmpty() && !recipe.FinalFluidInput.isEmpty())) return CheckRecipeResultRegistry.NO_RECIPE;
        for (int k : recipe.requireModules.keySet()) {
            if (k == 13 && recipe.requireModules.get(k) == 2 && !debug)
                return SimpleCheckRecipeResult.ofFailure("box_debugmode");
            if (!moduleActive[k] || recipe.requireModules.get(k) == 1 && moduleTier[k] != 1)
                return new ResultModuleRequirement(k, recipe.requireModules.get(k) == 1);
        }
        ItemContainer Icontainer = new ItemContainer();
        FluidContainer Fcontainer = new FluidContainer();
        List<ItemStack> totalInputItem = Icontainer.addItemStackList(inputItem, 1)
            .getItemStack();
        List<FluidStack> totalInputFluid = Fcontainer.addFluidStackList(inputFluid, 1)
            .getFluidStack();
        List<ItemStack> requireItem = deepCopyItemList(recipe.FinalItemInput);
        List<FluidStack> requireFluid = deepCopyFluidList(recipe.FinalFluidInput);
        BoxRecipe.ItemOnBox(totalInputItem, requireItem);
        BoxRecipe.FluidOnBox(totalInputFluid, requireFluid);
        inputItem.removeAll(Collections.singleton(null));
        inputFluid.removeAll(Collections.singleton(null));
        return (!recipe.FinalItemInput.isEmpty())
            ? (!recipe.FinalFluidInput.isEmpty()
                ? ((requireItem.isEmpty() && requireFluid.isEmpty()) ? runBox(inputItem, inputFluid)
                    : CheckRecipeResultRegistry.NO_RECIPE)
                : (requireItem.isEmpty() ? runBox(inputItem, inputFluid) : CheckRecipeResultRegistry.NO_RECIPE))
            : (requireFluid.isEmpty() ? runBox(inputItem, inputFluid) : CheckRecipeResultRegistry.NO_RECIPE);
    }

    /**
     * Run the box system.
     *
     * @param inputItem  All itemstack that input
     * @param inputFluid All fluidstack that input
     * @return true if the box starts
     */
    public CheckRecipeResult runBox(List<ItemStack> inputItem, List<FluidStack> inputFluid) {
        if (!moduleActive[12] || moduleTier[12] == 0) {
            if (getMaxInputEu() < recipe.FinalVoteage)
                return CheckRecipeResultRegistry.insufficientPower(recipe.FinalVoteage);
            lEUt = -recipe.FinalVoteage;
        }
        if (moduleActive[12] && moduleTier[12] == 1
            && !WirelessNetworkManager.addEUToGlobalEnergyMap(userUUID, -recipe.FinalVoteage * recipe.FinalTime)) {
            return SimpleCheckRecipeResult.ofFailure("no_wireless_power");
        }
        if (recipe.FinalTime >= Integer.MAX_VALUE - 1) return CheckRecipeResultRegistry.DURATION_OVERFLOW;
        calTime();
        if (this.lEUt >= Long.MAX_VALUE - 1) return CheckRecipeResultRegistry.POWER_OVERFLOW;
        mEfficiencyIncrease = 10000;
        mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
        List<ItemStack> requireItem = deepCopyItemList(recipe.FinalItemInput);
        List<FluidStack> requireFluid = deepCopyFluidList(recipe.FinalFluidInput);
        BoxRecipe.ItemOnBox(requireItem, inputItem);
        BoxRecipe.FluidOnBox(requireFluid, inputFluid);
        mOutputItems = recipe.FinalItemOutput.toArray(new ItemStack[0]);
        mOutputFluids = recipe.FinalFluidOutput.toArray(new FluidStack[0]);
        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void calTime() {
        if (lEUt == 0) {
            mMaxProgresstime = Math.max((int) Math.pow(recipe.FinalTime, 0.2), 10);
            return;
        }
        OverclockCalculator cal = new OverclockCalculator().setRecipeEUt(recipe.FinalVoteage)
            .setDuration((int) recipe.FinalTime)
            .setEUt(getMaxInputEu());
        switch (ringCount) {
            case 1:
                this.mMaxProgresstime = (int) recipe.FinalTime;
                return;
            case 2:
                break;
            case 3:
                cal.enablePerfectOC();
        }
        cal.calculate();
        this.lEUt = cal.getConsumption();
        this.mMaxProgresstime = cal.getDuration();
        if (this.lEUt > 0) {
            this.lEUt *= -1;
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    /**
     * I'd rather use CuttingFactory's texture,but you will kill me definitely.
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[114][0], TextureFactory.builder()
                .addIcon(boxActive)
                .extFacing()
                .build() };
            return new ITexture[] { casingTexturePages[114][0], TextureFactory.builder()
                .addIcon(boxInactive)
                .extFacing()
                .build() };
        }
        return new ITexture[] { casingTexturePages[114][0] };
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aTick == 1) {
            userUUID = getBaseMetaTileEntity().getOwnerUuid();
            WirelessNetworkManager.strongCheckOrAddUser(userUUID);
        }
    }

    /**
     * Make a pattern. Mostly from AE2 & AE2FC-GTNH.
     *
     * @param player Who is making it
     */
    private void makeAE2Pattern(EntityPlayer player, String item, String fluid) {
        if (item.equals("0") && fluid.equals("0")) {
            player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.chatmessage.6")));
            return;
        }
        if (mProgresstime == 0) {
            for (ItemStack pattern : getStoredInputs()) {
                if (Util.isPattern(pattern)) {
                    ItemStack outputPattern = pattern.copy();
                    if (recipe.FinalFluidOutput.isEmpty() && recipe.FinalFluidInput.isEmpty()) {
                        for (final ItemStack encodedPatternStack : AEApi.instance()
                            .definitions()
                            .items()
                            .encodedPattern()
                            .maybeStack(1)
                            .asSet()) {
                            outputPattern = encodedPatternStack;
                            NBTTagCompound encodedValue = recipe.RecipeToAE2ItemPattern(item);
                            outputPattern.setTagCompound(encodedValue);
                        }
                    } else {
                        outputPattern = new ItemStack(ItemAndBlockHolder.PATTERN);
                        FluidPatternDetails patternDetail = new FluidPatternDetails(outputPattern);
                        patternDetail.setInputs(recipe.transInputsToAE2Stuff());
                        patternDetail.setOutputs(recipe.transOutputsToAE2Stuff(item, fluid));
                        patternDetail.setCanBeSubstitute(0);
                        outputPattern = patternDetail.writeToStack();
                    }
                    outputPattern.stackTagCompound
                        .setString("author", player.getDisplayName() + i18n("tile.boxplusplus.boxinfo.16"));
                    pattern.stackSize -= 1;
                    mOutputItems = new ItemStack[] { outputPattern };
                    mMaxProgresstime = 100;
                    updateSlots();
                    return;
                }
            }
        }
        player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.chatmessage.8")));
    }

    /**
     * Build final recipe, but not write it.
     */
    public void buildRecipe() {
        ItemContainer inputItemContainer = new ItemContainer();
        ItemContainer outputItemContainer = new ItemContainer();
        FluidContainer inputFluidContainer = new FluidContainer();
        FluidContainer OutputFluidContainer = new FluidContainer();
        recipe = new BoxRecipe();
        routingMap.forEach(boxRoutings -> {
            inputItemContainer.addItemStackList(boxRoutings.InputItem, boxRoutings.Parallel);
            outputItemContainer
                .addItemStackList(boxRoutings.OutputItem, boxRoutings.OutputChance, boxRoutings.Parallel);
            inputFluidContainer.addFluidStackList(boxRoutings.InputFluid, boxRoutings.Parallel);
            OutputFluidContainer.addFluidStackList(boxRoutings.OutputFluid, boxRoutings.Parallel);
            recipe.FinalTime += boxRoutings.time * 5000L / (1 + Math.exp(-(boxRoutings.Parallel - 2000) / 320.0));
            recipe.FinalVoteage += boxRoutings.voltage;
            recipe.parallel += boxRoutings.Parallel;
            int[] machine = transMachinesToModule(boxRoutings);
            if (!recipe.requireModules.containsKey(machine[0]) || recipe.requireModules.get(machine[0]) < machine[1])
                recipe.requireModules.put(machine[0], machine[1]);
        });
        recipe.FinalItemInput = inputItemContainer.getItemStack();
        recipe.FinalItemOutput = outputItemContainer.getItemStack();
        recipe.FinalFluidInput = inputFluidContainer.getFluidStack();
        recipe.FinalFluidOutput = OutputFluidContainer.getFluidStack();
        BoxRecipe.ItemOnBox(recipe.FinalItemInput, recipe.FinalItemOutput);
        BoxRecipe.FluidOnBox(recipe.FinalFluidInput, recipe.FinalFluidOutput);
        if (recipe.parallel > 99900000) recipe.requireModules.put(13, 2);
        else if (recipe.parallel > 1280000) recipe.requireModules.put(13, 1);
        else if (recipe.parallel > 128000) recipe.requireModules.put(13, 0);
    }

    private boolean doubleRecipe() {
        int count = 0;
        for (BoxRoutings r : routingMap) {
            count += r.Parallel;
        }
        if (2 * count > maxParallel) return false;
        for (BoxRoutings r : routingMap) {
            r.Parallel *= 2;
        }
        return true;
    }

    private boolean halveRecipe() {
        for (BoxRoutings r : routingMap) {
            if ((r.Parallel & 1) == 1) return false;
        }
        for (BoxRoutings r : routingMap) {
            r.Parallel /= 2;
        }
        return true;
    }

    @Override
    public void onRemoval() {
        if (boxMap.containsValue(this)) boxMap.entrySet()
            .removeIf(
                t -> t.getValue()
                    .equals(this));
    }

    /**
     * We have many things need to store...
     */
    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        super.saveNBTData(NBT);
        NBT.setBoolean("Debug", debug);
        NBTTagCompound Routing = new NBTTagCompound();
        Routing.setInteger("ActiveRouting", routingMap.size());
        for (int i = 0; i < routingMap.size(); i++) {
            Routing.setTag(
                "Routing" + (i + 1),
                routingMap.get(i)
                    .routingToNbt());
        }
        NBTTagCompound nbtModuleSwitch = new NBTTagCompound();
        for (int i = 0; i < 14; i++) {
            nbtModuleSwitch.setBoolean(String.valueOf(i), moduleSwitch[i]);
        }
        NBTTagCompound nbtModuleActive = new NBTTagCompound();
        for (int i = 0; i < 14; i++) {
            nbtModuleActive.setBoolean(String.valueOf(i), moduleActive[i]);
        }
        NBT.setInteger("RingCountSet", ringCountSet);
        NBT.setInteger("RingCount", ringCount);
        NBT.setLong("maxParallel", maxParallel);
        NBT.setLong("maxRouting", maxRouting);
        NBT.setTag("ModuleSwitch", nbtModuleSwitch);
        NBT.setTag("ModuleActive", nbtModuleActive);
        NBT.setInteger("Status", routingStatus);
        NBT.setTag("Routing", Routing);
        NBT.setTag("BoxRecipe", recipe.RecipeToNBT());
    }

    /**
     * We have many things need to read...
     */
    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        super.loadNBTData(NBT);
        NBTTagCompound Routing = NBT.getCompoundTag("Routing");
        int ActiveRouting = Routing.getInteger("ActiveRouting");
        routingStatus = NBT.getInteger("Status");
        debug = NBT.getBoolean("Debug");
        routingMap.clear();
        for (int i = 0; i < ActiveRouting; i++) {
            routingMap.add(new BoxRoutings(Routing.getCompoundTag("Routing" + (i + 1))));
        }
        for (int i = 0; i < 14; i++) {
            moduleSwitch[i] = NBT.getCompoundTag("ModuleSwitch")
                .getBoolean(String.valueOf(i));
        }
        for (int i = 0; i < 14; i++) {
            moduleActive[i] = NBT.getCompoundTag("ModuleActive")
                .getBoolean(String.valueOf(i));
        }
        ringCount = NBT.getInteger("RingCount");
        ringCountSet = NBT.getInteger("RingCountSet");
        maxParallel = NBT.getInteger("maxParallel");
        maxRouting = NBT.getInteger("maxRouting");
        recipe = new BoxRecipe(NBT.getCompoundTag("BoxRecipe"));
    }

    /**
     * Add main UI with 4 bottoms
     */
    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(10, this::createInitialingWindow);
        buildContext.addSyncedWindow(11, this::createRoutingWindow);
        buildContext.addSyncedWindow(12, this::createFinalRecipeWindow);
        buildContext.addSyncedWindow(13, this::createModuleWindow);
        buildContext.addSyncedWindow(14, this::createSingleModuleWindow);
        buildContext.addSyncedWindow(15, this::createWikiWindow);
        buildContext.addSyncedWindow(16, this::createImportWindow);
        buildContext.addSyncedWindow(17, this::createExportPatternWindow);
        buildContext.addSyncedWindow(20, this::createClearWindow);
        Synchronize(builder);
        builder.widget(// Module
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(13);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_WHITELIST);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.01"))
                .setPos(86, 91))
            .widget(// SwitchRender
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!widget.isClient()) {
                        if (teBoxRing != null) {
                            teBoxRing.teRingSwitch = !teBoxRing.teRingSwitch;
                        }
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_CYCLIC);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.02"))
                    .setPos(130, 91))
            .widget(// Routing
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    Util.boxMap.put(
                        widget.getContext()
                            .getPlayer(),
                        this);
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(10);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_IMPORT);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.03"))
                    .setPos(108, 91))
            .widget(// WIKI
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> {
                        if (!widget.isClient()) widget.getContext()
                            .openSyncedWindow(15);
                    })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_INVERT_REDSTONE);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxwiki.1"))
                    .setPos(152, 91));
    }

    /**
     * Add main module UI
     */
    protected ModularWindow createModuleWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(250, 250);
        builder
            .setBackground(AdaptableUITexture.of(Tags.MODID, "textures/gui/ring" + ringCountSet + ".png", 695, 695, 4));
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        builder.widget(// Ring1
            new ButtonWidget().setOnClick((clickData, widget) -> {
                this.ringCountSet = 1;
                for (int i = 4; i < 14; i++) {
                    moduleSwitch[i] = false;
                    moduleActive[i] = false;
                    onMachineBlockUpdate();
                }
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(13);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.module.20"))
                .setPos(8, 8));
        builder.widget(// Ring2
            new ButtonWidget().setOnClick((clickData, widget) -> {
                this.ringCountSet = 2;
                for (int i = 8; i < 14; i++) {
                    moduleSwitch[i] = false;
                    moduleActive[i] = false;
                    onMachineBlockUpdate();
                }
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(13);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.module.21"))
                .setPos(8, 26));
        builder.widget(// Ring3
            new ButtonWidget().setOnClick((clickData, widget) -> {
                this.ringCountSet = 3;
                onMachineBlockUpdate();
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(13);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.module.22"))
                .setPos(8, 44))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(238, 0));
        builder.widget(// 3.1
            new ButtonWidget().setOnClick((clickData, widget) -> {
                moduleSN = 8;
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(14);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/09a.png", 16, 16, 4));
                    UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.module.9"))
                .setPos(8, 117)
                .setEnabled(ringCountSet == 3))
            .widget(// 3.2
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 9;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/10a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.10"))
                    .setPos(117, 11)
                    .setEnabled(ringCountSet == 3))
            .widget(// 3.3
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 10;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/11a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.11"))
                    .setPos(225, 117)
                    .setEnabled(ringCountSet == 3))
            /*
             * .widget(//3.4
             * new ButtonWidget().setOnClick(
             * (clickData, widget) -> {
             * })
             * .setSize(16, 16)
             * .setBackground(() -> {
             * List<UITexture> UI = new ArrayList<>();
             * UI.add(AdaptableUITexture.
             * of(Tags.MODID, "textures/gui/12a.png", 16, 16, 4));
             * UI.add(AdaptableUITexture.
             * of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
             * return UI.toArray(new IDrawable[0]);
             * })
             * .addTooltip(i18n("tile.boxplusplus.boxUI.module.12"))
             * .setPos(117, 223)
             * .setEnabled(RingCounts==3))
             */
            .widget(// 2.1
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 4;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/05a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/05b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.5"))
                    .setPos(35, 117)
                    .setEnabled(ringCountSet > 1))
            .widget(// 2.2
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 5;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/06a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.6"))
                    .setPos(117, 38)
                    .setEnabled(ringCountSet > 1))
            .widget(// 2.3
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 6;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/07a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.7"))
                    .setPos(195, 117)
                    .setEnabled(ringCountSet > 1))
            .widget(// 2.4
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 7;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/08a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.8"))
                    .setPos(117, 196)
                    .setEnabled(ringCountSet > 1))
            .widget(// 1.1
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 0;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.1"))
                    .setPos(64, 117))
            .widget(// 1.2
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 1;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/02a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.2"))
                    .setPos(117, 67))
            .widget(// 1.3
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 2;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/03a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.3"))
                    .setPos(164, 117))
            .widget(// 1.4
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 3;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/04a.png", 16, 16, 4));
                        UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/01b.png", 16, 16, 4));
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.4"))
                    .setPos(117, 167))
            .widget(// Up
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 12;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(AdaptableUITexture.of(Tags.MODID, "textures/gui/13a.png", 16, 16, 1))
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.13"))
                    .setPos(117, 107)
                    .setEnabled(ringCountSet == 3))
            .widget(// Down
                new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = 13;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(14);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(AdaptableUITexture.of(Tags.MODID, "textures/gui/14a.png", 16, 16, 1))
                    .addTooltip(i18n("tile.boxplusplus.boxUI.module.14"))
                    .setPos(117, 127)
                    .setEnabled(ringCountSet == 3));
        return builder.build();
    }

    /**
     * Add each module UI
     */
    protected ModularWindow createSingleModuleWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(150, 200);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(136, 3))
            .widget(
                new DrawableWidget().setDrawable(AdaptableUITexture.of(Tags.MODID, "textures/gui/dream.png", 16, 16, 1))
                    .setPos(5, 5)
                    .setSize(16, 16))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxUI.module." + (moduleSN + 1))).setPos(25, 9))
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        AdaptableUITexture.of(Tags.MODID, "textures/gui/" + (moduleSN + 1) + ".png", 100, 80, 1))
                    .setPos(20, 25)
                    .setSize(110, 73))
            .widget(
                new TextWidget(i18n("tile.boxplusplus.boxUI.module.context." + (moduleSN + 1) + "a"))
                    .setTextAlignment(TopCenter)
                    .setMaxWidth(130)
                    .setPos(10, 100))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxwiki.26")).setPos(20, 130))
            .widget(
                new TextWidget(i18n("tile.boxplusplus.boxUI.module.context." + (moduleSN + 1) + "b")).setMaxWidth(110)
                    .setPos(20, 140))
            .widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.module.24")
                        + i18n("tile.boxplusplus.boxUI.module.16" + (moduleSwitch[moduleSN] ? "" : "a"))
                        + (moduleTier[moduleSN] == 0 ? " (T1)" : " (T2)")).setPos(20, 175));
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            moduleSwitch[moduleSN] = true;
            onMachineBlockUpdate();
            if (!widget.isClient()) {
                widget.getWindow()
                    .closeWindow();
            }
        })
            .setSize(20, 20)
            .setBackground(() -> {
                List<UITexture> UI = new ArrayList<>();
                UI.add(GTUITextures.BUTTON_STANDARD);
                UI.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                return UI.toArray(new IDrawable[0]);
            })
            .addTooltip(i18n("tile.boxplusplus.boxUI.module.16"))
            .setPos(100, 170)
            .setEnabled(!moduleSwitch[moduleSN]))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                moduleSwitch[moduleSN] = false;
                moduleActive[moduleSN] = false;
                onMachineBlockUpdate();
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                }
            })
                .setSize(20, 20)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_CROSS);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.module.16a"))
                .setPos(100, 170)
                .setEnabled(moduleSwitch[moduleSN]));
        return builder.build();
    }

    /**
     * Add BOM UI
     *
     * @param player who is using the box
     */
    protected ModularWindow createInitialingWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(260, 215);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        randomSN = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomSN.add(new Random().nextInt(5) + 1);
        }
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_UP)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(
                new TextWidget(i18n("tile.boxplusplus.boxUI.05") + i18n("tile.boxplusplus.boxUI.06") + maxRouting)
                    .setPos(25, 9))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxUI.40") + maxParallel).setPos(170, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(240, 5));
        // Import GUI
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(16);
            }
        })
            .setSize(16, 16)
            .setBackground(() -> {
                List<UITexture> UI = new ArrayList<>();
                UI.add(GTUITextures.BUTTON_STANDARD);
                UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                return UI.toArray(new IDrawable[0]);
            })
            .addTooltip(i18n("tile.boxplusplus.boxUI.30"))
            .setPos(200, 25)
            .setEnabled(routingMap.size() == 0));
        // export
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (widget.isClient()) {
                NBTTagCompound Routing = new NBTTagCompound();
                Routing.setInteger("TotalRouting", routingMap.size());
                for (int i = 0; i < routingMap.size(); i++) {
                    Routing.setTag(
                        "Routing" + (i + 1),
                        routingMap.get(i)
                            .routingToUNbt());
                }
                GuiScreen.setClipboardString(serialize(Routing));
                player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.chatmessage.2")));
                player.closeScreen();
            }
        })
            .setSize(16, 16)
            .setBackground(() -> {
                List<UITexture> UI = new ArrayList<>();
                UI.add(GTUITextures.BUTTON_STANDARD);
                UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                return UI.toArray(new IDrawable[0]);
            })
            .addTooltip(i18n("tile.boxplusplus.boxUI.31"))
            .setPos(200, 25)
            .setEnabled(recipe.islocked));

        Scrollable routingContainer = new Scrollable().setVerticalScroll();
        for (int i = 0; i < routingMap.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, routingMap.get(i).RoutingMachine);
            row.widget(
                SlotWidget.phantom(drawitem, 0)
                    .disableInteraction()
                    .setSize(16, 16)
                    .setPos(0, 0));
            int finalI = i;
            row.widget(new TextWidget(i18n("tile.boxplusplus.boxUI.07") + (i + 1)).setPos(0, 4))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    moduleSN = finalI + 1;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(11);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.09"))
                    .setPos(0, 0))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!clickData.shift) return;
                    moduleSN = finalI + 1;
                    routingMap.remove(finalI);
                    if (!widget.isClient()) {
                        widget.getWindow()
                            .closeWindow();
                        widget.getContext()
                            .openSyncedWindow(10);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.26"))
                    .setPos(0, 0));
            routingContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, 20 * i));
        }
        if (!recipe.islocked) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            row.widget(new TextWidget(i18n("tile.boxplusplus.boxUI.07") + (routingMap.size() + 1)).setPos(0, 0))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    BoxRoutings.checkRouting(this);
                    if (!widget.isClient()) {
                        player.closeScreen();
                        GTUIInfos.openGTTileEntityUI(getBaseMetaTileEntity(), player);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxUI.08") + (routingMap.size() + 1))
                    .setPos(0, 0));
            routingContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, 20 * (routingMap.size())));
        }
        builder.widget(
            routingContainer.setPos(21, 20)
                .setSize(110, 180));
        // Error X
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CROSS)
                .setPos(140, 71)
                .setSize(24, 24)
                .addTooltip(i18n("tile.boxplusplus.boxUI.ErrorCode." + routingStatus))
                .setEnabled(routingStatus != 0 && !recipe.islocked))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                    .setPos(140, 71)
                    .setSize(36, 36)
                    .addTooltip(i18n("tile.boxplusplus.boxUI.19"))
                    .setEnabled(routingStatus == 0 && !recipe.islocked));
        // Build Recipe
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!recipe.islocked) buildRecipe();
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(12);
            }
        })
            .setSize(32, 32)
            .setBackground(() -> {
                List<UITexture> UI = new ArrayList<>();
                UI.add(GTUITextures.BUTTON_STANDARD);
                UI.add(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                return UI.toArray(new IDrawable[0]);
            })
            .addTooltip(i18n("tile.boxplusplus.boxUI.20"))
            .setPos(140, 26)
            .setEnabled(!routingMap.isEmpty() && !recipe.islocked))
            // Double Recipe
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    routingStatus = doubleRecipe() ? 0 : 10;
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(10);
                }
            })
                .setSize(14, 14)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/double.png", 16, 16, 1));
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.33"))
                .setPos(175, 26)
                .setEnabled(!routingMap.isEmpty() && !recipe.islocked))
            // Halve Recipe
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    routingStatus = halveRecipe() ? 0 : 9;
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(10);
                }
            })
                .setSize(14, 14)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/halve.png", 16, 16, 1));
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.34"))
                .setPos(175, 44)
                .setEnabled(!routingMap.isEmpty() && !recipe.islocked))
            // export AE pattern
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(17);
                }
            })
                .setSize(14, 14)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/AE.png", 16, 16, 1));
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.36"))
                .setPos(175, 26)
                .setEnabled(recipe.islocked))
            // clear recipe
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(20);
                }
            })
                .setSize(14, 14)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(AdaptableUITexture.of(Tags.MODID, "textures/gui/clear.png", 16, 16, 1));
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.41"))
                .setPos(175, 44)
                .setEnabled(recipe.islocked))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(12);
                }
            })
                .setSize(32, 32)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_WHITELIST);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.21"))
                .setPos(140, 26)
                .setEnabled(recipe.islocked));
        return builder.build();
    }

    /**
     * Add Each Routing UI.
     *
     * @param player who is using the box
     */
    protected ModularWindow createRoutingWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(220, 200);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxUI.10") + moduleSN).setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(200, 5));
        Scrollable recipeContainer = new Scrollable().setVerticalScroll();
        int posY = 0;
        for (int i = 0; i < routingMap.get(moduleSN - 1).InputItem.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, routingMap.get(moduleSN - 1).InputItem.get(i));
            row.widget(
                SlotWidget.phantom(drawitem, 0)
                    .disableInteraction());
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.11") + (i + 1)
                        + ": "
                        + routingMap.get(moduleSN - 1).InputItem.get(i)
                            .getDisplayName()).setPos(0, 4));
            recipeContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, posY));
            posY += 16;
        }
        for (int i = 0; i < routingMap.get(moduleSN - 1).InputFluid.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            row.widget(
                FluidSlotWidget
                    .phantom(
                        new FluidTank(
                            routingMap.get(moduleSN - 1).InputFluid.get(i),
                            routingMap.get(moduleSN - 1).InputFluid.get(i).amount),
                        true)
                    .setInteraction(false, false));
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.12") + (i + 1)
                        + ": "
                        + routingMap.get(moduleSN - 1).InputFluid.get(i)
                            .getLocalizedName()).setPos(0, 4));
            recipeContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, posY));
            posY += 16;
        }
        for (int i = 0; i < routingMap.get(moduleSN - 1).OutputItem.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, routingMap.get(moduleSN - 1).OutputItem.get(i));
            row.widget(
                SlotWidget.phantom(drawitem, 0)
                    .disableInteraction());
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.13") + (i + 1)
                        + ": "
                        + routingMap.get(moduleSN - 1).OutputItem.get(i)
                            .getDisplayName()
                        + "("
                        + routingMap.get(moduleSN - 1).OutputChance.get(i) / 10000.0
                        + ")").setPos(0, 4));
            recipeContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, posY));
            posY += 16;
        }
        for (int i = 0; i < routingMap.get(moduleSN - 1).OutputFluid.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            row.widget(
                FluidSlotWidget
                    .phantom(
                        new FluidTank(
                            routingMap.get(moduleSN - 1).OutputFluid.get(i),
                            routingMap.get(moduleSN - 1).OutputFluid.get(i).amount),
                        true)
                    .setInteraction(false, false));
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.14") + (i + 1)
                        + ": "
                        + routingMap.get(moduleSN - 1).OutputFluid.get(i)
                            .getLocalizedName()).setPos(0, 4));
            recipeContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setPos(0, posY));
            posY += 16;
        }
        builder.widget(
            recipeContainer.setPos(20, 24)
                .setSize(180, 110))
            .widget(
                new TextWidget("------------------------------------------").setMaxWidth(10000)
                    .setPos(21, 132));
        ItemStackHandler drawitem = new ItemStackHandler(1);
        drawitem.setStackInSlot(0, routingMap.get(moduleSN - 1).RoutingMachine);
        builder.widget(
            SlotWidget.phantom(drawitem, 0)
                .disableInteraction()
                .setSize(16, 16)
                .setPos(21, 146));
        builder.widget(
            new TextWidget(
                i18n("tile.boxplusplus.boxUI.15") + routingMap.get(moduleSN - 1).RoutingMachine.getDisplayName())
                    .setPos(45, 140));
        builder.widget(
            new TextWidget(i18n("tile.boxplusplus.boxUI.16") + routingMap.get(moduleSN - 1).voltage + "eu/t")
                .setPos(45, 150));
        builder.widget(
            new TextWidget(
                i18n("tile.boxplusplus.boxUI.17") + routingMap.get(moduleSN - 1).time / 20.00
                    + "s ("
                    + routingMap.get(moduleSN - 1).time
                    + "tick)").setPos(45, 160));
        builder.widget(
            new TextWidget(new Text(i18n("tile.boxplusplus.boxUI.23"))).setTextAlignment(Alignment.Center)
                .setSize(30, 16)
                .setPos(21, 170))
            .widget(
                new TextFieldWidget().setGetterInt(() -> routingMap.get(moduleSN - 1).Parallel)
                    .setSetterInt(val -> routingMap.get(moduleSN - 1).Parallel = val)
                    .setNumbers(1, maxParallel)
                    .setTextColor(Color.WHITE.normal)
                    .setTextAlignment(Alignment.Center)
                    .addTooltip(i18n("tile.boxplusplus.boxUI.24"))
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                    .setSize(60, 12)
                    .setPos(50, 171)
                    .setEnabled(!recipe.islocked))
            .widget(
                new TextWidget(new Text(String.valueOf(routingMap.get(moduleSN - 1).Parallel)))
                    .setTextAlignment(Alignment.Center)
                    .setSize(60, 12)
                    .setPos(50, 171)
                    .setEnabled(recipe.islocked));
        return builder.build();
    }

    /**
     * Importing UI.
     */
    protected ModularWindow createImportWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(300, 48);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        TextFieldWidget textField = new TextFieldWidget() {

            @Override
            public boolean onKeyPressed(char character, int keyCode) {
                if (KeyboardUtil.isKeyComboCtrlV(keyCode)) {
                    handler.getText()
                        .add(GuiScreen.getClipboardString());
                    handler.onChanged();
                    return true;
                }
                return super.onKeyPressed(character, keyCode);
            }

            @org.jetbrains.annotations.NotNull
            public String getText() {
                if (handler.getText()
                    .isEmpty()) {
                    return "";
                }
                return handler.getText()
                    .get(0);
            }

            @Override
            public void onRemoveFocus() {
                if (handler.getText()
                    .size() > 1) {
                    player.closeScreen();
                    player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.chatmessage.3")));
                }
                this.renderer.setCursor(false);
                this.cursorTimer = 0;
                this.scrollOffset = 0;
                if (syncsToServer()) {
                    syncToServer(1, buffer -> NetworkUtils.writeStringSafe(buffer, getText()));
                }
            }
        };
        return builder.widget(new TextWidget(i18n("tile.boxplusplus.boxUI.46")).setPos(5, 5))
            .widget(
                textField.setMaxLength(10000)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.dark(1))
                    .setFocusOnGuiOpen(true)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                    .setPos(5, 16)
                    .setSize(250, 16))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    String ls = textField.getText();
                    NBTTagCompound routing = deserialize(ls);
                    try {
                        if (routing != null) {
                            int count = routing.getInteger("TotalRouting");
                            if (count > maxRouting) {
                                routingStatus = 8;
                                player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.chatmessage.4")));
                                return;
                            }
                            routingMap.clear();
                            for (int i = 1; i <= count; i++) {
                                routingMap.add(new BoxRoutings(routing.getCompoundTag("Routing" + i), true));
                            }
                            player.addChatMessage(
                                new ChatComponentText(
                                    i18n("tile.boxplusplus.chatmessage.5")
                                        .replaceFirst("%count", String.valueOf(count))));
                            routingStatus = 0;
                        } else {
                            player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.chatmessage.6")));
                        }
                    } finally {
                        player.closeScreen();
                    }
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.30"))
                .setPos(270, 16))
            .build();
    }

    protected ModularWindow createExportPatternWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(168, 125);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        TextFieldWidget inputItem = new TextFieldWidget().setValidator(var -> Util.validator(recipe, var, false));
        TextFieldWidget inputFluid = new TextFieldWidget().setValidator(var -> Util.validator(recipe, var, true));
        return builder.widget(new TextWidget(i18n("tile.boxplusplus.boxUI.48")).setPos(5, 45))
            .widget(
                inputItem.setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.dark(1))
                    .setFocusOnGuiOpen(false)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                    .setPos(12, 10)
                    .addTooltip(i18n("tile.boxplusplus.boxUI.13"))
                    .setSize(60, 12))
            .widget(
                inputFluid.setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.dark(1))
                    .setFocusOnGuiOpen(false)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                    .setPos(96, 10)
                    .addTooltip(i18n("tile.boxplusplus.boxUI.14"))
                    .setSize(60, 12))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    String itemInput = inputItem.getText();
                    String fluidInput = inputFluid.getText();
                    makeAE2Pattern(player, itemInput, fluidInput);
                    player.closeScreen();
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.36"))
                .setPos(76, 25))
            .build();
    }

    /**
     * Clear UI.
     */
    protected ModularWindow createClearWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(146, 60 + (randomSN.size() == 1 ? 15 : 0));
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        builder
            .widget(
                new DrawableWidget().setDrawable(AdaptableUITexture.of(Tags.MODID, "textures/gui/clear.png", 16, 16, 1))
                    .setPos(5, 5)
                    .setSize(16, 16))
            .widget(
                new TextWidget(
                    (randomSN.size() != 1 ? i18n("tile.boxplusplus.boxUI.42") : i18n("tile.boxplusplus.boxUI.43"))
                        + i18n("tile.boxplusplus.boxUI.44")
                        + EnumChatFormatting.RED
                        + randomSN.get(randomSN.size() - 1)).setPos(25, 5));
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (finalI == randomSN.get(randomSN.size() - 1)) {
                    if (randomSN.size() == 1) {
                        if (!widget.isClient()) {
                            routingMap.clear();
                            recipe = new BoxRecipe();
                            routingPageCode = 1;
                            routingStatus = 0;
                            player.closeScreen();
                            player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.boxUI.45")));
                        }
                    } else {
                        randomSN.remove(randomSN.size() - 1);
                        if (!widget.isClient()) {
                            widget.getWindow()
                                .closeWindow();
                            widget.getContext()
                                .openSyncedWindow(20);
                        }
                    }
                } else {
                    player.closeScreen();
                    player.addChatMessage(new ChatComponentText(i18n("tile.boxplusplus.boxUI.47")));
                }
            })
                .setSize(24, 24)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(
                        AdaptableUITexture.of(
                            Tags.MODID,
                            "textures/gui/number%.png".replaceFirst("%", String.valueOf(finalI)),
                            275,
                            81,
                            1));
                    return UI.toArray(new IDrawable[0]);
                })
                .setPos(28 * (i - 1) + 5, 30 + (randomSN.size() == 1 ? 15 : 0)));
        }
        return builder.build();
    }

    /**
     * Add final recipe UI. This recipe can now be changed after being locked.
     *
     * @param player who is using the box
     */
    protected ModularWindow createFinalRecipeWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(360, 220);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxUI.22")).setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(345, 5));
        Scrollable inputContainer = new Scrollable().setVerticalScroll()
            .setHorizontalScroll();
        int posY = 0;
        int maxX = 0;
        for (int i = 0; i < recipe.FinalItemInput.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, recipe.FinalItemInput.get(i));
            maxX = Math.max(
                9 * recipe.FinalItemInput.get(i)
                    .getDisplayName()
                    .length(),
                maxX);
            row.widget(
                SlotWidget.phantom(drawitem, 0)
                    .disableInteraction());
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.11") + (i + 1)
                        + ": "
                        + recipe.FinalItemInput.get(i)
                            .getDisplayName()).setPos(0, 4));
            inputContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setSize(75 + maxX, 16)
                    .setPos(0, posY));
            posY += 16;
        }
        maxX = 0;
        for (int i = 0; i < recipe.FinalFluidInput.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            maxX = Math.max(
                9 * recipe.FinalFluidInput.get(i)
                    .getLocalizedName()
                    .length(),
                maxX);
            row.widget(
                FluidSlotWidget
                    .phantom(new FluidTank(recipe.FinalFluidInput.get(i), recipe.FinalFluidInput.get(i).amount), true)
                    .setInteraction(false, false));
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.12") + (i + 1)
                        + ": "
                        + recipe.FinalFluidInput.get(i)
                            .getLocalizedName()).setPos(0, 4));
            inputContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setSize(50 + maxX, 16)
                    .setPos(0, posY));
            posY += 16;
        }
        builder.widget(
            inputContainer.setPos(20, 24)
                .setSize(150, 130));
        Scrollable outputContainer = new Scrollable().setVerticalScroll()
            .setHorizontalScroll();
        posY = 0;
        maxX = 0;
        for (int i = 0; i < recipe.FinalItemOutput.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            ItemStackHandler drawitem = new ItemStackHandler(1);
            drawitem.setStackInSlot(0, recipe.FinalItemOutput.get(i));
            maxX = Math.max(
                9 * recipe.FinalItemOutput.get(i)
                    .getDisplayName()
                    .length(),
                maxX);
            row.widget(
                SlotWidget.phantom(drawitem, 0)
                    .disableInteraction());
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.13") + (i + 1)
                        + ": "
                        + recipe.FinalItemOutput.get(i)
                            .getDisplayName()).setPos(0, 4));
            outputContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setSize(75 + maxX, 16)
                    .setPos(0, posY));
            posY += 16;
        }
        maxX = 0;
        for (int i = 0; i < recipe.FinalFluidOutput.size(); i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            maxX = Math.max(
                9 * recipe.FinalFluidOutput.get(i)
                    .getLocalizedName()
                    .length(),
                maxX);
            row.widget(
                FluidSlotWidget
                    .phantom(new FluidTank(recipe.FinalFluidOutput.get(i), recipe.FinalFluidOutput.get(i).amount), true)
                    .setInteraction(false, false));
            row.widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.14") + (i + 1)
                        + ": "
                        + recipe.FinalFluidOutput.get(i)
                            .getLocalizedName()).setPos(0, 4));
            outputContainer.widget(
                row.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSpace(4)
                    .setSize(75 + maxX, 16)
                    .setPos(0, posY));
            posY += 16;
        }
        builder.widget(
            outputContainer.setPos(180, 24)
                .setSize(150, 130))
            .widget(
                new TextWidget("---------------------------------------------------------------" + "---------------")
                    .setMaxWidth(10000)
                    .setPos(20, 153));

        builder
            .widget(new TextWidget(i18n("tile.boxplusplus.boxUI.16") + recipe.FinalVoteage + " eu/t").setPos(20, 160))
            .widget(
                new TextWidget(
                    i18n("tile.boxplusplus.boxUI.17") + recipe.FinalTime / 20.00 + "s (" + recipe.FinalTime + "tick)")
                        .setPos(20, 172));
        builder.widget(
            new TextWidget(i18n("tile.boxplusplus.boxUI.29") + recipe.parallel).setMaxWidth(180)
                .setPos(20, 184));
        builder.widget(
            new TextWidget(i18n("tile.boxplusplus.boxUI.32").replace("%max", String.valueOf(maxParallel)))
                .setMaxWidth(180)
                .setPos(25, 196)
                .setEnabled(recipe.parallel > maxParallel));

        StringBuilder modules = new StringBuilder();
        modules.append(i18n("tile.boxplusplus.boxUI.27"));
        for (int i : recipe.requireModules.keySet()) {
            modules.append(moduleActive[i] ? "" : "§4")
                .append(i18n("tile.boxplusplus.boxUI.module." + (i + 1)))
                .append(" T")
                .append(recipe.requireModules.get(i) + 1)
                .append(" ")
                .append(" §r| ");
        }
        builder.widget(
            new TextWidget(modules.toString()).setTextAlignment(TopLeft)
                .setMaxWidth(130)
                .setPos(200, 160))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                recipe.islocked = true;
                if (!widget.isClient()) {
                    player.closeScreen();
                    GTUIInfos.openGTTileEntityUI(getBaseMetaTileEntity(), player);
                }
            })
                .setSize(20, 20)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxUI.25"))
                .setPos(170, 160)
                .setEnabled(!recipe.islocked && recipe.parallel <= maxParallel));
        return builder.build();
    }

    /**
     * Add Wiki. I like it.
     */
    protected ModularWindow createWikiWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(300, 210);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        Synchronize(builder);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_NEI)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxwiki.1")).setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(285, 5))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxwiki.2")).setPos(25, 30))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                wikiPageCode = 3;
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(15);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_INVERT_REDSTONE);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxwiki.3"))
                .setPos(30, 45))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                wikiPageCode = 4;
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(15);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxwiki.4"))
                .setPos(80, 45))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                wikiPageCode = 5;
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(15);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxwiki.5"))
                .setPos(130, 45))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                wikiPageCode = 6;
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(15);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_PROGRESS);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxwiki.6"))
                .setPos(180, 45))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                wikiPageCode = 7;
                if (!widget.isClient()) {
                    widget.getWindow()
                        .closeWindow();
                    widget.getContext()
                        .openSyncedWindow(15);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> UI = new ArrayList<>();
                    UI.add(GTUITextures.BUTTON_STANDARD);
                    UI.add(GTUITextures.OVERLAY_BUTTON_WHITELIST);
                    return UI.toArray(new IDrawable[0]);
                })
                .addTooltip(i18n("tile.boxplusplus.boxwiki.7"))
                .setPos(230, 45))
            .widget(new TextWidget(i18n("tile.boxplusplus.boxwiki." + wikiPageCode)).setPos(135, 70));
        getwikiByIndex(builder);
        return builder.build();
    }

    // An error code
    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements.widget(
            new TextWidget(i18n("tile.boxplusplus.boxError." + machineError[0])).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(machineError[0] != 0))
            .widget(
                new TextWidget(i18n("tile.boxplusplus.boxUI.module." + machineError[1]))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(machineError[1] != 0));
    }

    // Help wiki show pages
    private void getwikiByIndex(ModularWindow.Builder builder) {
        switch (wikiPageCode) {
            case 3 -> builder.widget(
                new TextWidget(i18n("tile.boxplusplus.boxwiki.8")).setMaxWidth(260)
                    .setPos(25, 85));
            case 4 -> {
                builder.widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.9")).setMaxWidth(260)
                        .setPos(25, 85));
                builder.widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.10") + i18n("tile.boxplusplus.boxwiki.11"))
                        .setTextAlignment(TopLeft)
                        .setMaxWidth(260)
                        .setPos(25, 95));
            }
            case 5 -> {
                builder.widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.12")).setTextAlignment(TopLeft)
                        .setMaxWidth(260)
                        .setPos(25, 85));
                builder.widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.13") + i18n("tile.boxplusplus.boxwiki.14"))
                        .setTextAlignment(TopLeft)
                        .setMaxWidth(260)
                        .setPos(25, 115));
                builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    wikiPageCode = 50;
                    if (!widget.isClient()) {
                        widget.getWindow()
                            .closeWindow();
                        widget.getContext()
                            .openSyncedWindow(15);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_DOWN);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxwiki.0"))
                    .setPos(135, 175));
            }
            case 6 -> builder.widget(
                new TextWidget(i18n("tile.boxplusplus.boxwiki.16")).setTextAlignment(TopLeft)
                    .setMaxWidth(260)
                    .setPos(25, 85))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.17")).setMaxWidth(260)
                        .setPos(25, 105))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.18")).setMaxWidth(260)
                        .setPos(25, 115))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.19")).setMaxWidth(260)
                        .setPos(25, 125))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.20")).setMaxWidth(260)
                        .setPos(25, 135))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.21")).setMaxWidth(260)
                        .setPos(25, 145))
                .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    wikiPageCode = 51;
                    if (!widget.isClient()) {
                        widget.getWindow()
                            .closeWindow();
                        widget.getContext()
                            .openSyncedWindow(15);
                    }
                })
                    .setSize(16, 16)
                    .setBackground(() -> {
                        List<UITexture> UI = new ArrayList<>();
                        UI.add(GTUITextures.BUTTON_STANDARD);
                        UI.add(GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_DOWN);
                        return UI.toArray(new IDrawable[0]);
                    })
                    .addTooltip(i18n("tile.boxplusplus.boxwiki.0"))
                    .setPos(135, 175));
            case 7 -> {
                builder.widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.24")).setMaxWidth(260)
                        .setPos(25, 85));
                for (int i = 1; i < 15; i++) {
                    int finalI = i;
                    builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                        moduleSN = finalI;
                        if (!widget.isClient()) {
                            widget.getWindow()
                                .closeWindow();
                            widget.getContext()
                                .openSyncedWindow(15);
                        }
                    })
                        .setSize(16, 16)
                        .setBackground(() -> {
                            List<UITexture> UI = new ArrayList<>();
                            UI.add(GTUITextures.BUTTON_STANDARD);
                            UI.add(GTUITextures.OVERLAY_BUTTON_EMIT_REDSTONE);
                            return UI.toArray(new IDrawable[0]);
                        })
                        .addTooltip(i18n("tile.boxplusplus.boxUI.module." + i))
                        .setPos(10 + 18 * i, 100));
                }
                builder
                    .widget(
                        new TextWidget(
                            i18n("tile.boxplusplus.boxwiki.25") + i18n("tile.boxplusplus.boxUI.module." + moduleSN))
                                .setMaxWidth(260)
                                .setPos(25, 120))
                    .widget(
                        new TextWidget(
                            i18n("tile.boxplusplus.boxwiki.26")
                                + i18n("tile.boxplusplus.boxUI.module.context." + moduleSN + "b")).setMaxWidth(260)
                                    .setPos(25, 130))
                    .widget(
                        new TextWidget(
                            i18n("tile.boxplusplus.boxwiki.27")
                                + i18n("tile.boxplusplus.boxUI.module.context." + moduleSN + "c")).setMaxWidth(260)
                                    .setPos(25, 150))
                    .widget(
                        new TextWidget(
                            i18n("tile.boxplusplus.boxwiki.28")
                                + i18n("tile.boxplusplus.boxUI.module.context." + moduleSN + "d")).setMaxWidth(260)
                                    .setPos(25, 160))
                    .widget(
                        new TextWidget(
                            i18n("tile.boxplusplus.boxwiki.29")
                                + i18n("tile.boxplusplus.boxUI.module.context." + moduleSN + "e")).setMaxWidth(260)
                                    .setPos(25, 170));
            }
            case 50 -> builder.widget(
                new TextWidget(i18n("tile.boxplusplus.boxwiki.15")).setTextAlignment(TopLeft)
                    .setMaxWidth(260)
                    .setPos(25, 85));
            case 51 -> builder.widget(
                new TextWidget(i18n("tile.boxplusplus.boxwiki.22")).setMaxWidth(260)
                    .setPos(25, 85))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.23")).setMaxWidth(260)
                        .setPos(180, 85))
                .widget(
                    new DrawableWidget()
                        .setDrawable(AdaptableUITexture.of(Tags.MODID, "textures/gui/time.png", 275, 81, 1))
                        .setPos(20, 105)
                        .setSize(130, 42))
                .widget(
                    new DrawableWidget()
                        .setDrawable(AdaptableUITexture.of(Tags.MODID, "textures/gui/voteage.png", 124, 81, 1))
                        .setPos(190, 105)
                        .setSize(62, 40))
                .widget(
                    new TextWidget(i18n("tile.boxplusplus.boxwiki.52")).setMaxWidth(260)
                        .setPos(25, 165));
        }
    }

    /**
     * Synchronize all data to server. Do not change pack size!
     */
    public void Synchronize(ModularWindow.Builder builder) {
        builder.widget(new FakeSyncWidget.ListSyncer<>(() -> randomSN, var1 -> {
            randomSN.clear();
            randomSN.addAll(var1);
        }, PacketBuffer::writeInt, PacketBuffer::readInt))
            .widget(new FakeSyncWidget.ListSyncer<>(() -> routingMap, var1 -> {
                routingMap.clear();
                routingMap.addAll(var1);
            }, (buffer, j) -> {
                try {
                    buffer.writeNBTTagCompoundToBuffer(j.routingToNbt());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, buffer -> {
                try {
                    return new BoxRoutings(buffer.readNBTTagCompoundFromBuffer());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }))
            .widget(new FakeSyncWidget<>(() -> recipe, var1 -> recipe = var1, (buffer, j) -> {
                try {
                    buffer.writeNBTTagCompoundToBuffer(recipe.RecipeToNBT());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, buffer -> {
                try {
                    return new BoxRecipe(buffer.readNBTTagCompoundFromBuffer());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> routingStatus, var1 -> routingStatus = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> routingPageCode, var1 -> routingPageCode = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> machineError[0], var1 -> machineError[0] = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> machineError[1], var1 -> machineError[1] = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> ringCountSet, var1 -> ringCountSet = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> ringCount, var1 -> ringCount = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> maxParallel, var1 -> maxParallel = var1))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> maxRouting, var1 -> maxRouting = var1));
        for (int i = 0; i < 14; i++) {
            int finalI = i;
            builder
                .widget(
                    new FakeSyncWidget.BooleanSyncer(() -> moduleSwitch[finalI], var1 -> moduleSwitch[finalI] = var1))
                .widget(
                    new FakeSyncWidget.BooleanSyncer(() -> moduleActive[finalI], var1 -> moduleActive[finalI] = var1))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> moduleTier[finalI], var1 -> moduleTier[finalI] = var1));
        }
    }
}
