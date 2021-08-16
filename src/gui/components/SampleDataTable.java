package gui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import backend.data.TableData;

/**
 * SampleDataTable is a custom JTable extension to display Sample percentage breakdown
 */
public class SampleDataTable extends JTable {
    // samplePie: parent sample pie to refer to when constructing the table
    private final SamplePie samplePie;

    /**
     * Constructs SampleDataTable
     * @param samplePie sample pie to build the table from
     */
    public SampleDataTable(SamplePie samplePie) {
        this.samplePie = samplePie;
        String[][] tableContent = samplePie.getParentSample().getPercentagesAsTable();
        this.setModel(new DefaultTableModel(tableContent, TableData.percentageHeader));
        this.design();
        // assigning row clicks
        this.rowSelection();
    }

    /**
     * provides design of SampleDataTable
     */
    private void design() {
        // horizontally aligning columns
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);

        this.setForeground(TableData.CONTENT_FOREGROUND_COLOR);
        this.setBackground(TableData.BACKGROUND_COLOR);
        this.getTableHeader().setForeground(TableData.HEADER_FOREGROUND_COLOR);
        this.getTableHeader().setBackground(TableData.BACKGROUND_COLOR);

        this.setDefaultRenderer(String.class, cellRenderer);
        // setting table as read only
        this.setDefaultEditor(Object.class, null);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // setting custom column size
        this.getColumnModel().getColumn(1).setPreferredWidth(2);
    }

    /**
     * regulates row selection operation of SampleDataTable
     */
    private void rowSelection() {
        this.getSelectionModel().addListSelectionListener(e -> {
            String populationType = this.getValueAt(this.getSelectedRow(), 0).toString();
            samplePie.selectSection(populationType);
        });
    }

}
