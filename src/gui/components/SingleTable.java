package gui.components;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.LinkedHashMap;

import backend.data.TableData;

/**
 * SingleTable is used to display Single calculator output.
 */
public class SingleTable extends SampleTable {
    // rowSorter: table sorter for SampleTable
    public TableRowSorter<TableModel> rowSorter;

    /**
     * Constructs SingleTable
     * @param singleMap single map to refer to
     */
    public SingleTable(LinkedHashMap<String, Double> singleMap) {
        String[] header = TableData.singleHeader;
        String[][] content = TableData.getSingleTableContent(singleMap);
        DefaultTableModel tableModel = new DefaultTableModel(content, header);
        this.setModel(tableModel);
        this.design();
        rowSorter = new TableRowSorter<>(this.getModel());
        this.setRowSorter(rowSorter);
    }

    /**
     * regulates row selection operation of SingleTable
     */
    @Override
    public void rowSelection() {}

}
