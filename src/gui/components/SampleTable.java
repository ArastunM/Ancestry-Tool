package gui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import app.App;
import backend.Sample;
import backend.SampleAverage;
import backend.data.TableData;
import gui.DefaultObjects;
import gui.windows.DatabaseWindow;
import gui.windows.SampleWindow;

/**
 * SampleTable is a custom JTable extension to display samples
 */
public class SampleTable extends JTable {
    // rowSorter: table sorter for SampleTable
    public TableRowSorter<TableModel> rowSorter;

    // selectedRow: contains currently selected row number
    private int selectedRow = -1;

    /**
     * Constructs SampleTable
     */
    public SampleTable() {
        String[] header = TableData.header;
        String[][] content = TableData.getSortedTableContent();
        DefaultTableModel tableModel = new DefaultTableModel(content, header);
        this.setModel(tableModel);
        this.design();
        this.rowSelection();
        rowSorter = new TableRowSorter<>(this.getModel());
        this.setRowSorter(rowSorter);
    }

    /**
     * Constructs SampleTable based on given sort
     * @param bySort sort type to refer to
     */
    public SampleTable(String bySort) {
        String[] header = TableData.header;
        String[][] content;
        content = TableData.getSortedTableContent(bySort);

        DefaultTableModel tableModel = new DefaultTableModel(content, header);
        this.setModel(tableModel);
        this.design();
        this.rowSelection();
        rowSorter = new TableRowSorter<>(this.getModel());
        this.setRowSorter(rowSorter);
    }

    /**
     * Constructs SampleTable based on components of SampleAverage
     * @param sampleAverage SampleAverage to refer to
     */
    public SampleTable(SampleAverage sampleAverage) {
        String[] header = TableData.componentHeader;
        String[][] content = sampleAverage.getComponentsAsTable();
        DefaultTableModel tableModel = new DefaultTableModel(content, header);
        this.setModel(tableModel);
        this.design();
        this.rowSelection();
        rowSorter = new TableRowSorter<>(this.getModel());
        this.setRowSorter(rowSorter);
    }

    /**
     * provides the design of SampleTable
     */
    public void design() {
        // horizontally aligning columns
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.setDefaultRenderer(String.class, cellRenderer);

        // setting table colors
        this.setForeground(TableData.CONTENT_FOREGROUND_COLOR);
        this.setBackground(TableData.BACKGROUND_COLOR);
        this.getTableHeader().setForeground(TableData.HEADER_FOREGROUND_COLOR);
        this.getTableHeader().setBackground(TableData.BACKGROUND_COLOR);

        // setting table as read only
        this.setDefaultEditor(Object.class, null);
    }

    /**
     * regulates row selection operation of SampleTable
     */
    public void rowSelection() {
        this.getSelectionModel().addListSelectionListener(e -> {
            if (selectedRow != this.getSelectedRow()) {
                selectedRow = this.getSelectedRow();
                String id = this.getValueAt(this.getSelectedRow(), 1).toString();

                if (DatabaseWindow.removeSampleBtn.getBackground().equals(DefaultObjects.BTN_CLICK_COLOR)) {
                    String warningContent = "Are you sure to remove the sample " + id;
                    ConfirmPopup popup = new ConfirmPopup("Warning", warningContent, id);
                    popup.pop();
                }
                // viewing Sample Window
                else {
                    Sample clickedSample = Sample.getSample(id);
                    if (App.sampleWindow.parentSample != clickedSample) {
                        App.sampleWindow = new SampleWindow(clickedSample);
                        App.sampleWindow.switchTo();
                    }
                }
            }
        });
    }
}
