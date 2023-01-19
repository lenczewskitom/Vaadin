package com.kodilla.vaadin.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "history", layout = MainLayout.class)
@PageTitle("History | Savings App")
public class HistoryView extends HorizontalLayout {
}
