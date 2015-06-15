package de.project.ice.pathtool;

import aurelienribon.ui.components.ArStyle;
import aurelienribon.ui.components.PaintedPanel;
import aurelienribon.ui.css.Style;
import aurelienribon.ui.css.swing.SwingStyle;
import de.project.ice.utils.notifications.AutoListModel;
import de.project.ice.utils.notifications.ObservableList;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import res.Res;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class MainWindow extends javax.swing.JFrame {
    private final Canvas canvas;
    private final ObservableList<ImageModel> images = new ObservableList<ImageModel>();
    private final ListCellRenderer emittersListCellRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ImageModel img = (ImageModel) value;
            label.setText(img.file.getPath());
            return label;
        }
    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton deleteBtn;
    private CompactCheckBox drawConnectionsChk;
    private CompactCheckBox drawAreaChk;
    private CompactCheckBox drawPathChk;
    private javax.swing.JPanel headerPanel;
    private JList imagesList;
    private JLabel jLabel1;

    // -------------------------------------------------------------------------
    // Generated stuff
    // -------------------------------------------------------------------------
    private JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JButton loadBtn;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JPanel projectPanel;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton exportBtn;
    private final ListSelectionListener emittersListSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            ImageModel img = (ImageModel) imagesList.getSelectedValue();
            canvas.setImage(img);
            deleteBtn.setEnabled(img != null);
            saveBtn.setEnabled(img != null);
            exportBtn.setEnabled(img != null);
        }
    };
    private CompactSlider spriteOpacitySlider;
    private JLabel versionLabel;

    public MainWindow(final Canvas canvas, Component canvasCmp) {
        this.canvas = canvas;

        setContentPane(new PaintedPanel());
        getContentPane().setLayout(new BorderLayout());
        initComponents();
        renderPanel.add(canvasCmp, BorderLayout.CENTER);

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });
        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });

        spriteOpacitySlider.setChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.spriteOpacity = spriteOpacitySlider.getValue();
            }
        });
        drawConnectionsChk.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.drawConnections = drawConnectionsChk.isSelected();
            }
        });
        drawAreaChk.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.drawArea = drawAreaChk.isSelected();
            }
        });
        drawPathChk.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.drawPath = drawPathChk.isSelected();
            }
        });

        imagesList.setModel(new AutoListModel<ImageModel>(images));
        imagesList.setCellRenderer(emittersListCellRenderer);
        imagesList.addListSelectionListener(emittersListSelectionListener);
        emittersListSelectionListener.valueChanged(null);

        SwingStyle.init();
        ArStyle.init();
        Style.registerCssClasses(getContentPane(), ".rootPanel");
        Style.registerCssClasses(renderPanel, ".titledPanel", "#renderPanel");
        Style.registerCssClasses(projectPanel, ".titledPanel", "#projectPanel");
        Style.registerCssClasses(optionsPanel, ".titledPanel", "#optionsPanel");
        Style.registerCssClasses(headerPanel, ".headerPanel");
        Style.registerCssClasses(versionLabel, ".versionLabel");
        Style.apply(getContentPane(), new Style(Res.getUrl("css/style.css")));
    }

    private void add() {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Choose one or more images");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription() {
                return "Images (png, jpg, gif)";
            }

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String ext = FilenameUtils.getExtension(f.getName());
                return ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif");
            }
        });

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File file : chooser.getSelectedFiles()) {
                try {
                    ImageModel img = new ImageModel(file);
                    images.add(img);
                    Collections.sort(images, new Comparator<ImageModel>() {
                        @Override
                        public int compare(ImageModel o1, ImageModel o2) {
                            String s1 = o1.file.getPath();
                            String s2 = o2.file.getPath();
                            if (s1.compareToIgnoreCase(s2) < 0) return -1;
                            if (s1.compareToIgnoreCase(s2) > 0) return 1;
                            return 0;
                        }
                    });
                    imagesList.setSelectedValue(img, rootPaneCheckingEnabled);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Cannot get the canonical path of file:\n" + file.getPath());
                }
            }
        }
    }

    private void delete() {
        ImageModel img = (ImageModel) imagesList.getSelectedValue();
        images.remove(img);
        imagesList.clearSelection();
    }

    private void load() {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Choose the file to read");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ProjectIce Pathfinding Project", "pfp");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                images.clear();
                images.addAll(ImageModelIo.load(file));
                imagesList.clearSelection();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot load the project, reason is:\n" + ex.getMessage());
            }
        }
    }

    private void save() {
        JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ProjectIce Pathfinding Project", "pfp");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Choose the file to write or overwrite");

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                if (!file.getName().endsWith(".pfp"))
                    file = new File(file.getAbsolutePath() + ".pfp");
                ImageModelIo.save(file, images);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot save the project, reason is:\n" + ex.getMessage());
            }
        }
    }

    private void export() {

        JTextArea textarea= null;
        try {
            textarea = new JTextArea(ImageModelIo.export((ImageModel) imagesList.getSelectedValue()));
            textarea.setLineWrap(true);
            textarea.setEditable(false);
            JOptionPane.showMessageDialog(null, textarea, "Patharea data", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot export project, reason is:\n" + e.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        renderPanel = new javax.swing.JPanel();
        projectPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        addBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jToolBar7 = new javax.swing.JToolBar();
        saveBtn = new javax.swing.JButton();
        exportBtn = new javax.swing.JButton();
        loadBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        imagesList = new JList();
        optionsPanel = new javax.swing.JPanel();
        spriteOpacitySlider = new CompactSlider();
        drawConnectionsChk = new CompactCheckBox();
        drawPathChk = new CompactCheckBox();
        drawAreaChk = new CompactCheckBox();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        versionLabel = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ProjectIce PathTool");

        renderPanel.setLayout(new BorderLayout());

        projectPanel.setLayout(new BorderLayout());

        headerPanel.setLayout(new BorderLayout());

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        addBtn.setIcon(new javax.swing.ImageIcon(Res.class.getResource("gfx/ic_add.png"))); // NOI18N
        addBtn.setText("Add image(s)");
        addBtn.setFocusable(false);
        addBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addBtn.setMargin(new java.awt.Insets(2, 3, 2, 3));
        jToolBar4.add(addBtn);

        deleteBtn.setIcon(new javax.swing.ImageIcon(Res.class.getResource("gfx/ic_delete.png"))); // NOI18N
        deleteBtn.setFocusable(false);
        deleteBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(deleteBtn);

        headerPanel.add(jToolBar4, BorderLayout.WEST);

        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);

        saveBtn.setIcon(new javax.swing.ImageIcon(Res.class.getResource("gfx/ic_save.png"))); // NOI18N
        saveBtn.setText("Save");
        saveBtn.setFocusable(false);
        saveBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveBtn.setMargin(new java.awt.Insets(2, 3, 2, 3));
        jToolBar7.add(saveBtn);

        exportBtn.setIcon(new javax.swing.ImageIcon(Res.class.getResource("gfx/ic_save.png"))); // NOI18N
        exportBtn.setText("Export");
        exportBtn.setFocusable(false);
        exportBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exportBtn.setMargin(new java.awt.Insets(2, 3, 2, 3));
        jToolBar7.add(exportBtn);

        loadBtn.setIcon(new javax.swing.ImageIcon(Res.class.getResource("gfx/ic_open.png"))); // NOI18N
        loadBtn.setText("Load");
        loadBtn.setFocusable(false);
        loadBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToolBar7.add(loadBtn);

        headerPanel.add(jToolBar7, BorderLayout.EAST);

        projectPanel.add(headerPanel, BorderLayout.NORTH);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        imagesList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        imagesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(imagesList);

        projectPanel.add(jScrollPane1, BorderLayout.CENTER);

        spriteOpacitySlider.setValue(0.5F);

        drawConnectionsChk.setSelected(false);
        drawConnectionsChk.setText("Draw Connections");

        drawPathChk.setSelected(false);
        drawPathChk.setText("Draw Paths");

        drawAreaChk.setSelected(true);
        drawAreaChk.setText("Draw Area");

        jLabel1.setText("Image opacity");

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
                optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spriteOpacitySlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                                .addComponent(drawAreaChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(drawConnectionsChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(drawPathChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
                optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(spriteOpacitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(drawAreaChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(drawConnectionsChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(drawPathChk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(Res.class.getResource("gfx/logo.png"))); // NOI18N

        versionLabel.setText("v0.0.1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(renderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(projectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                                        .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(versionLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel2)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(projectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(versionLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addComponent(renderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables

}
