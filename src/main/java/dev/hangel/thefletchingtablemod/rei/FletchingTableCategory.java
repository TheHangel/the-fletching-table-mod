package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.TheFletchingTableMod;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class FletchingTableCategory implements DisplayCategory<BasicDisplay> {
    public static final Identifier TEXTURE =
            Identifier.of(TheFletchingTableMod.MOD_ID, "textures/gui/fletching_table_gui.png");

    public static final CategoryIdentifier<FletchingTableDisplay> FLETCHING_TABLE_DISPLAY_CATEGORY =
            CategoryIdentifier.of(TheFletchingTableMod.MOD_ID, "fletching_table");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return FLETCHING_TABLE_DISPLAY_CATEGORY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.minecraft.fletching_table");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.FLETCHING_TABLE.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.getX(), startPoint.getY(), 175, 82)));

        widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 25, startPoint.getY() + 34))
            .entries(display.getInputEntries().get(0)).markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 78, startPoint.getY() + 34))
            .entries(display.getInputEntries().get(1)).markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.getX() + 132, startPoint.getY() + 34))
            .entries(display.getOutputEntries().getFirst()).markOutput());

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }
}
