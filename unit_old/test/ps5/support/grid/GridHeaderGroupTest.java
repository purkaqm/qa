package ps5.support.grid;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Ignore;
import org.junit.Test;

import ps5.support.grid.GridHeaderCell.HeaderGroup;
import ps5.wbs.model.sections.ColumnDefinition.HeaderGroupDefinition;


class TestGridHeader extends GridSerializationController.GridHeaderBuilder {

    @Override
    protected Object serializeHeaderGroupCell(HeaderGroup group) {
        return new JSONObject()
                .put("id", group.getId());
    }

    @Override
    protected Object serializeHeaderCell(GridHeaderCell cell) {
        return new JSONObject()
                .put("id", cell.getId());
    }
}


/**
 * Exercises Grid header serialization with grouping.
 * Some resulting JSON currently has to be verified by visual inspection.
 * 
 * The test header idea: 
 * <p><blockquote><pre>
 *  |  Name  |   Allocation & Availability   |       Costs       |     
 *  |        |     Apr 01    |    Apr 02     | Estimate | Actual |
 *  |        | Alloc | Avail | Alloc | Avail |          |        |
 * </pre></blockquote></p>
 *
 */
public class GridHeaderGroupTest {
    
    final HeaderGroup ALLOC_AVAIL = new HeaderGroupDefinition("AL_AV")
            .withDisplayName("Allocation & Availability").define();
    final HeaderGroup ALLOC_AVAIL_APR_01 = new HeaderGroupDefinition("AL_AV_APR-01")
            .withDisplayName("Apr 01").withParentGroup(ALLOC_AVAIL).define();
    final HeaderGroup ALLOC_AVAIL_APR_02 = new HeaderGroupDefinition("AL_AV_APR-02")
            .withDisplayName("Apr 02").withParentGroup(ALLOC_AVAIL).define();
    final HeaderGroup COSTS = new HeaderGroupDefinition("COSTS")
            .withDisplayName("Costs").define();
    
    GridHeaderCell makeHeaderCell(String id) {
        return new GridHeaderCell(id);
    }
    GridHeaderCell makeHeaderCell(String id, HeaderGroup group) {
        GridHeaderCell cell = new GridHeaderCell(id);
        cell.setHeaderGroup(group);
        return cell;
    }
    
    TestGridHeader gridHeader = new TestGridHeader();

    @Test
    public void testSample() {
        HeaderGroup ALLOC_AVAIL = new HeaderGroupDefinition("AL_AV")
                    .withDisplayName("Allocation / Availability")
                    .define();
        HeaderGroup ALLOC_AVAIL_DATE = new HeaderGroupDefinition("AL_AV_APR-01")
                    .withDisplayName("Apr 01").withParentGroup(ALLOC_AVAIL)
                    .define();
        List<GridHeaderCell> cells = Arrays.asList(
                makeHeaderCell("NAME"),
                makeHeaderCell("APR-01_AL", ALLOC_AVAIL_DATE),
                makeHeaderCell("APR-01_AV", ALLOC_AVAIL_DATE)
        );
        gridHeader.add(cells);
        assertEquals(cells.size(), gridHeader.getTotalColumnCount());
        assertEquals(2, gridHeader.getTopLevelColumnCount());
        assertEquals(3, gridHeader.getRowCount());
        
        JSONObject result = gridHeader.serialize();
        String actual = result.toString(4);
        String expected = (
                     "{'rows': [\n" +
                     "    [\n" +
                     "        {\n" +
                     "            'rowspan': 3,\n" +
                     "            'header': {'id': 'NAME'}\n" +
                     "        },\n" +
                     "        {\n" +
                     "            'colspan': 2,\n" +
                     "            'group': {'id': 'AL_AV'}\n" +
                     "        }\n" +
                     "    ],\n" +
                     "    [{\n" +
                     "        'colspan': 2,\n" +
                     "        'group': {'id': 'AL_AV_APR-01'}\n" +
                     "    }],\n" +
                     "    [\n" +
                     "        {'header': {'id': 'APR-01_AL'}},\n" +
                     "        {'header': {'id': 'APR-01_AV'}}\n" +
                     "    ]\n" +
                     "]}"
                ).replace('\'', '"');
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNoGrouping() {
        List<GridHeaderCell> cells = Arrays.asList(
                makeHeaderCell("NAME"),
                makeHeaderCell("APR-01_AL"),
                makeHeaderCell("APR-01_AV")
        );
        gridHeader.add(cells);
        assertEquals(cells.size(), gridHeader.getTotalColumnCount());
        assertEquals(3, gridHeader.getTopLevelColumnCount());
        assertEquals(1, gridHeader.getRowCount());
        
        JSONObject result = gridHeader.serialize();
        String actual = result.toString(4);
        String expected = (
                "{'rows': [[\n" +
                "    {'header': {'id': 'NAME'}},\n" +
                "    {'header': {'id': 'APR-01_AL'}},\n" +
                "    {'header': {'id': 'APR-01_AV'}}\n" +
                "]]}"
            ).replace('\'', '"');
        assertEquals(expected, actual);
    }
    
    @Ignore("Requires manual verification")
    @Test
    public void testOneLevelGrouping() {
        final HeaderGroup APR_01 = new HeaderGroupDefinition("APR-01").define();
        List<GridHeaderCell> cells = Arrays.asList(
                makeHeaderCell("NAME"),
                makeHeaderCell("APR-01_AL", APR_01),
                makeHeaderCell("APR-01_AV", APR_01)
        );
        gridHeader.add(cells);
        assertEquals(cells.size(), gridHeader.getTotalColumnCount());
        assertEquals(2, gridHeader.getTopLevelColumnCount());
        assertEquals(2, gridHeader.getRowCount());
        
        JSONObject result = gridHeader.serialize();
        assertEquals("please verify", result.toString(4));
    }
    
    @Ignore("Requires manual verification")
    @Test
    public void testUniformDepthGrouping() {
        List<GridHeaderCell> cells = Arrays.asList(
                makeHeaderCell("NAME"),
                makeHeaderCell("APR-01_AL", ALLOC_AVAIL_APR_01),
                makeHeaderCell("APR-01_AV", ALLOC_AVAIL_APR_01),
                makeHeaderCell("APR-02_AL", ALLOC_AVAIL_APR_02),
                makeHeaderCell("APR-02_AV", ALLOC_AVAIL_APR_02),
                makeHeaderCell("ESTIMATE", COSTS),
                makeHeaderCell("ACTUAL", COSTS)
        );
        gridHeader.add(cells);
        assertEquals(cells.size(), gridHeader.getTotalColumnCount());
        assertEquals(3, gridHeader.getTopLevelColumnCount());
        assertEquals(3, gridHeader.getRowCount());
        
        JSONObject result = gridHeader.serialize();
        assertEquals("please verify", result.toString(4));
    }
    
    @Ignore("Requires manual verification")
    @Test
    public void testMixedDepthGrouping() {
        List<GridHeaderCell> cells = Arrays.asList(
                makeHeaderCell("NAME"),
                makeHeaderCell("APR-01_AL", ALLOC_AVAIL_APR_01),
                makeHeaderCell("APR-01_AV", ALLOC_AVAIL_APR_01),
                makeHeaderCell("TOTAL_AL", ALLOC_AVAIL),
                makeHeaderCell("TOTAL_AV", ALLOC_AVAIL),
                makeHeaderCell("ESTIMATE", COSTS),
                makeHeaderCell("ACTUAL", COSTS)
        );
        gridHeader.add(cells);
        assertEquals(cells.size(), gridHeader.getTotalColumnCount());
        assertEquals(3, gridHeader.getTopLevelColumnCount());
        assertEquals(3, gridHeader.getRowCount());
        
        JSONObject result = gridHeader.serialize();
        assertEquals("please verify", result.toString(4));
    }
}
