package dev.hangel.thefletchingtablemod.rei;

import dev.hangel.thefletchingtablemod.Constants;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class FletchingTableREICategory implements DisplayCategory<FletchingTableREIDisplay> {
    public static final CategoryIdentifier<FletchingTableREIDisplay> CATEGORY_ID =
            CategoryIdentifier.of(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fletching_table"));

    private static final Identifier GUI_TEXTURE =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/fletching_table_gui.png");

    @Override
    public CategoryIdentifier<? extends FletchingTableREIDisplay> getCategoryIdentifier() {
        return CATEGORY_ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.minecraft.fletching_table");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.FLETCHING_TABLE);
    }

    @Override
    public int getDisplayWidth(FletchingTableREIDisplay display) {
        return 176;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public List<Widget> setupDisplay(FletchingTableREIDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();

        Point origin = new Point(bounds.getCenterX() - 88, bounds.getCenterY() - 40);

        widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, origin.x, origin.y, 0, 0, 176, 80, 256, 256));

        widgets.add(Widgets.createSlot(new Point(origin.x + 25, origin.y + 34))
                .entries(display.getInputEntries().get(0))
                .markInput());

        widgets.add(Widgets.createSlot(new Point(origin.x + 78, origin.y + 34))
                .entries(display.getInputEntries().get(1))
                .markInput());

        widgets.add(Widgets.createSlot(new Point(origin.x + 132, origin.y + 34))
                .entries(display.getOutputEntries().getFirst())
                .markOutput());

        return widgets;
    }
}
