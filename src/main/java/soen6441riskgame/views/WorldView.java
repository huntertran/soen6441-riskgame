package soen6441riskgame.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import soen6441riskgame.models.Country;
import soen6441riskgame.singleton.GameBoard;

/**
 * Demonstrates jung support for drawing edge labels that can be positioned at any point along the
 * edge, and can be rotated to be parallel with the edge.
 * 
 * @author Tom Nelson
 * 
 */
public class WorldView extends JApplet implements Observer {
    private static final long serialVersionUID = -6077157664507049647L;

    /**
     * the graph
     */
    Graph<Country, Number> graph;

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<Country, Number> vv;

    VertexLabelRenderer vertexLabelRenderer;

    ScalingControl scaler = new CrossoverScalingControl();

    public void redraw() {
        // create a simple graph for the demo
        graph = new SparseMultigraph<Country, Number>();
        Country[] v = createVertices();

        createEdges(v);

        Layout<Country, Number> layout = new CircleLayout<Country, Number>(graph);
        vv = new VisualizationViewer<Country, Number>(layout, new Dimension(600, 400));
        vv.setBackground(Color.white);

        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(graph));

        vertexLabelRenderer = vv.getRenderContext().getVertexLabelRenderer();

        vv.getRenderContext().setEdgeDrawPaintTransformer(
                                                          new PickableEdgePaintTransformer<Number>(vv.getPickedEdgeState(),
                                                                                                   Color.black,
                                                                                                   Color.cyan));
        vv.getRenderContext().setVertexFillPaintTransformer(
                                                            new PickableVertexPaintTransformer<Country>(vv.getPickedVertexState(),
                                                                                                        Color.red,
                                                                                                        Color.yellow));

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        // add my listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());

        // create a frome to hold the graph
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        Container content = getContentPane();
        content.add(panel);

        final DefaultModalGraphMouse<Integer, Number> graphMouse = new DefaultModalGraphMouse<Integer, Number>();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });

        MutableDirectionalEdgeValue mv = new MutableDirectionalEdgeValue(.5, .7);
        vv.getRenderContext().setEdgeLabelClosenessTransformer(mv);

        JPanel zoomPanel = new JPanel(new GridLayout(0, 1));
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Scale"));
        zoomPanel.add(plus);
        zoomPanel.add(minus);

        Box controls = Box.createHorizontalBox();

        controls.add(zoomPanel);
        content.add(controls, BorderLayout.SOUTH);
    }

    /**
     * subclassed to hold two BoundedRangeModel instances that are used by JSliders to move the edge
     * label positions
     * 
     * @author Tom Nelson
     *
     *
     */
    class MutableDirectionalEdgeValue extends ConstantDirectionalEdgeValueTransformer<Country, Number> {
        BoundedRangeModel undirectedModel = new DefaultBoundedRangeModel(5, 0, 0, 10);
        BoundedRangeModel directedModel = new DefaultBoundedRangeModel(7, 0, 0, 10);

        public MutableDirectionalEdgeValue(double undirected, double directed) {
            super(undirected, directed);
            undirectedModel.setValue((int) (undirected * 10));
            directedModel.setValue((int) (directed * 10));

            undirectedModel.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setUndirectedValue(new Double(undirectedModel.getValue() / 10f));
                    vv.repaint();
                }
            });
            directedModel.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setDirectedValue(new Double(directedModel.getValue() / 10f));
                    vv.repaint();
                }
            });
        }
    }

    /**
     * create some vertices
     * 
     * @param count how many to create
     * @return the Vertices in an array
     */
    private Country[] createVertices() {
        int count = GameBoard.getInstance().getGameBoardMap().getCountries().size();
        if (count == 0) {
            return null;
        }

        Country[] v = new Country[count];
        for (int i = 0; i < count; i++) {
            v[i] = GameBoard.getInstance().getGameBoardMap().getCountries().get(i);
            graph.addVertex(v[i]);
        }

        return v;
    }

    /**
     * create edges for this demo graph
     * 
     * @param v an array of Vertices to connect
     */
    void createEdges(Country[] v) {
        if (v == null || v.length == 0) {
            return;
        }

        for (Country country : v) {
            ArrayList<Country> neighbors = country.getNeighbors();
            for (Country neighbor : neighbors) {
                graph.addEdge(new Double(Math.random()), neighbor, country);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        redraw();
    }
}