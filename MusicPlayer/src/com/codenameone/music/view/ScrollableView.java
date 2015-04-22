package com.codenameone.music.view;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.List;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Style;
import userclasses.StateMachine;
import java.util.Map;

abstract class ScrollableView {
    protected StateMachine ui;

    abstract List getList(Form f);

    protected void beforeShowAfterModel(final Form f) {
        Display.getInstance().callSerially(new Runnable() {
            @Override
            public void run() {
                List list = getList(f);

                // Scroll down until the saved position is reached (therefore we used the last pixel on the screen ;)) It's here to support switching view-modes (portrait, landscape)
                if (f.getClientProperty(list.getName() + "ScrollY") != null) {
                    int scrollY = Integer.parseInt(f.getClientProperty(list.getName() + "ScrollY") + "");

                    /**
                     * This applies f.e. if you clicked on the embeded player. Then the selected element is 0 and the animation will lead the user to the top of the list again, even so we have set a scroll-position.
                     * We therefore have to find a visible items in a list and set it as selected, even so it wasn't.
                     */
                    if (list.getModel().getSelectedIndex() == 0) {
                        // This is just to get the height of a component! Phew!
                        int browseItemsSingleElementHeight = calculateElementSize(f).getHeight();

                        if (browseItemsSingleElementHeight > 0) {
                            // Based on the scroll-position and the height of a single element (all are equally height) we can calculate the first fully visible element.
                            list.setSelectedIndex(((int) Math.ceil(scrollY / browseItemsSingleElementHeight)) + 1);
                        }
                    }

                    list.scrollRectToVisible(0, scrollY, 1, list.getHeight(), null);
                }
            }
        });
    }

    /**
     * Called when the form is created to set the current state.
     */
    public void setState(Form f, Map state) {
        List list = getList(f);

        f.putClientProperty(list.getName()+"ScrollY", state.get(list.getName()+"ScrollY"));

        /**
         * On loading the view for the list, it animates a scroll to the selected position.
         * This position will overwrite the previous ScrollY position if the element is out-of-scope.
         *
         * @see UIBuilder.setContainerStateImpl(Container cnt, Hashtable state)
         */
        String cmpName = (String)state.get(StateMachine.FORM_STATE_KEY_FOCUS);
        if(cmpName != null) {
            Component c = ui.findByName(cmpName, f);
            if(c != null) {
                c.requestFocus();
                if(c instanceof List) {
                    Integer i = (Integer)state.get(StateMachine.FORM_STATE_KEY_SELECTION);
                    if(i != null) {
                        ((List)c).setSelectedIndex(i.intValue());
                    }
                }
            }
        }
    }

    /**
     * Called when the form is closed to get the current state and store it.
     */
    public void getState(Form f, Map h) {
        List list = getList(f);

        // Save the position of scrolling. Saving the first visible pixel position.
        h.put(list.getName()+"ScrollY", list.getScrollY());
    }

    /**
     * Just a minimal copy, because the method is not public :(
     * @see com.codename1.ui.List
     */
    private Dimension calculateElementSize(Form f) {
        List list = getList(f);

        Object renderingPrototype = list.getRenderingPrototype();
        if (renderingPrototype != null) {
            Component unselected = list.getRenderer().getListCellRendererComponent(list, renderingPrototype, 0, false);
            return getPreferredSizeWithMargin(unselected);
        }
        int width = 0;
        int height = 0;
        int elements = Math.min(list.getListSizeCalculationSampleCount(), list.getModel().getSize());
        int marginY = 0;
        int marginX = 0;
        for (int iter = 0; iter < elements; iter++) {
            Component cmp = list.getRenderer().getListCellRendererComponent(list, list.getModel().getItemAt(iter), iter, false);
            if(cmp instanceof Container) {
                cmp.setShouldCalcPreferredSize(true);
            }
            Dimension d = cmp.getPreferredSize();
            width = Math.max(width, d.getWidth());
            height = Math.max(height, d.getHeight());
            if(iter == 0) {
                Style s = cmp.getStyle();
                marginY = s.getMargin(List.TOP) + s.getMargin(List.BOTTOM);
                marginX = s.getMargin(List.LEFT) + s.getMargin(List.RIGHT);
            }
        }
        return new Dimension(width + marginX, height + marginY);
    }

    private Dimension getPreferredSizeWithMargin(Component cmp) {
        Dimension d = cmp.getPreferredSize();
        Style s = cmp.getStyle();
        return new Dimension(d.getWidth() +s.getMargin(List.LEFT) + s.getMargin(List.RIGHT), d.getHeight() + s.getMargin(List.TOP) + s.getMargin(List.BOTTOM));
    }
}
