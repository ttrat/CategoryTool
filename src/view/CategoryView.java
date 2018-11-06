package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import model.Constants;
import model.Patient;
import model.Provider;
import controller.CategoryController;

public class CategoryView extends JPanel implements WindowListener{
	
	private static final long serialVersionUID = -624816261262115480L;
	
	private static final String ADD_ACTION = "add";
	private static final String LOAD_ACTION = "load";
	private static final String CREATE_ACTION = "create";
	private static final String SAVE_ACTION = "save";
	private static final String EXIT_ACTION = "exit";
	private static final String VERSION_NOTES_ACTION = "notes";
	private static final String AUDIT_NOTES_ACTION = "audit_notes";
	private static final String ADD_PROVIDER_ACTION = "add_provider";
	private static final String CHANGE_PROVIDER_ACTION = "change_provider";
	
//	private static final String DELETE_ACTION = "delete";
	private static final String DATE_CHANGE_ACTION = "Date selected";
	
	private final ComboItem[] categories = {new ComboItem(-1, Constants.IMAGING_CATEGORIES.get(-1)),
													new ComboItem(0, Constants.IMAGING_CATEGORIES.get(0)),
													new ComboItem(1, Constants.IMAGING_CATEGORIES.get(1)),
													new ComboItem(2, Constants.IMAGING_CATEGORIES.get(2)),
													new ComboItem(3, Constants.IMAGING_CATEGORIES.get(3)),
													new ComboItem(4, Constants.IMAGING_CATEGORIES.get(4)),
													new ComboItem(5, Constants.IMAGING_CATEGORIES.get(5)),
													new ComboItem(6, Constants.IMAGING_CATEGORIES.get(6))};
	private final ComboItem[] ultrasounds = {new ComboItem(-1, Constants.ULTRASOUND_CATEGORIES.get(-1)),
													new ComboItem(0, Constants.ULTRASOUND_CATEGORIES.get(0)),
													new ComboItem(1, Constants.ULTRASOUND_CATEGORIES.get(1)),
													new ComboItem(2, Constants.ULTRASOUND_CATEGORIES.get(2)),
													new ComboItem(3, Constants.ULTRASOUND_CATEGORIES.get(3)),
													new ComboItem(4, Constants.ULTRASOUND_CATEGORIES.get(4)),
													new ComboItem(5, Constants.ULTRASOUND_CATEGORIES.get(5)),
													new ComboItem(6, Constants.ULTRASOUND_CATEGORIES.get(6))};
	private final ComboItem[] biopsies = {new ComboItem(-1, Constants.BIOPSY_RESULTS.get(-1)),
													new ComboItem(0, Constants.BIOPSY_RESULTS.get(0)),
													new ComboItem(1, Constants.BIOPSY_RESULTS.get(1)),
													new ComboItem(2, Constants.BIOPSY_RESULTS.get(2)),
													new ComboItem(3, Constants.BIOPSY_RESULTS.get(3)),
													new ComboItem(4, Constants.BIOPSY_RESULTS.get(4)),
													new ComboItem(5, Constants.BIOPSY_RESULTS.get(5)),
													new ComboItem(6, Constants.BIOPSY_RESULTS.get(6)),
													new ComboItem(7, Constants.BIOPSY_RESULTS.get(7)),
													new ComboItem(8, Constants.BIOPSY_RESULTS.get(8))};
	
	private final JTable patientTable;
	private final CategoryController controller;
	private JFrame frame;
	private final PatientTableModel patientTableModel;
	private final StatisticsTableModel statsTableModel;
	private final AuditTableModel auditTableModel;
	private JComboBox<Provider> providerBox;
	
	private JDatePickerImpl startDate;
	private JDatePickerImpl endDate;
	
	public CategoryView(){
		patientTableModel = new PatientTableModel();
		patientTable = new PatientTable(patientTableModel);
		
		patientTableModel.addTableModelListener(new TableModelListener() {

		      public void tableChanged(TableModelEvent e) {
		    	  if(e.getType() == TableModelEvent.UPDATE){
		    		  /*if(patientTableModel.getValueAt(e.getFirstRow(), PatientTableModel.DELETED_COLUMN).equals(Boolean.FALSE)){
		    			  patientTableModel.setValueAt(Boolean.TRUE, e.getFirstRow(), PatientTableModel.EDITED_COLUMN);
		    		  }*/
		    		  updateAction();
//		    		  updateGUI();
		    	  }
		      }
	    });
		
		/*TableRowSorter<PatientTableModel> sorter = new TableRowSorter<PatientTableModel>(patientTableModel);
		patientTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);*/
		
		
		statsTableModel = new StatisticsTableModel();
		auditTableModel = new AuditTableModel();
		
		providerBox = new JComboBox<Provider>();
		
		controller = new CategoryController();
		
		initialize();
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
	}
	
	private void addAction(){
		
		JTextField patientName = new JTextField();
		JComboBox<ComboItem> screening = new JComboBox<ComboItem>(categories);
		JComboBox<ComboItem> diagnostic = new JComboBox<ComboItem>(categories);
		JComboBox<ComboItem> ultrasound = new JComboBox<ComboItem>(ultrasounds);
		JComboBox<ComboItem> biopsy = new JComboBox<ComboItem>(biopsies);
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		JTextField provider = new JTextField();
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Patient Name"),
				patientName,
				new JLabel("Screening Category"),
				screening,
				new JLabel("Diagnostic Category"),
				diagnostic,
				new JLabel("Ultrasound Result"),
				ultrasound,
				new JLabel("Biopsy Result"),
				biopsy,
				new JLabel("Date of Imaging"),
				datePicker,
				new JLabel("Provider"),
				provider
				
		};
		
		int c = JOptionPane.showConfirmDialog(null, inputs, "Add Patient", JOptionPane.PLAIN_MESSAGE);
		
		if(c != JOptionPane.CLOSED_OPTION){
			
			List<String> names = new ArrayList<String>();
			for(Patient patient : controller.getPatients()){
				names.add(patient.getName());
			}
			
			if(patientName.getText().equals("")){
				JOptionPane.showMessageDialog(null, "Cannot add patient with empty name", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(datePicker.getModel().getValue() == null){
				JOptionPane.showMessageDialog(null, "Cannot add patient with empty imaging date", "Error", JOptionPane.ERROR_MESSAGE);
			}
			/*else if(names.contains(patientName.getText())){
				JOptionPane.showMessageDialog(null, "Patient name already exists", "Error", JOptionPane.ERROR_MESSAGE);
			}*/
			
			else{
				Patient newPatient = new Patient(patientName.getText(), ((ComboItem)screening.getSelectedItem()).getValue(), ((ComboItem)diagnostic.getSelectedItem()).getValue(), ((ComboItem)ultrasound.getSelectedItem()).getValue(), ((ComboItem)biopsy.getSelectedItem()).getValue(), retrieveDate(datePicker), provider.getText(), "");
				
				newPatient.setAdded(true);
				controller.getPatients().add(newPatient);
				updateGUI();
			}
		}
		
	}
	
	private void loadAction(){
		
		controller.actionBrowseFile();
		
		updateGUI();
	}
	
	/*private void newRecordAction() {
		
		JTextField recordFileName = new JTextField();
		
		final JComponent[] inputs = new JComponent[] {
				new JLabel("New Record File Name"),
				recordFileName
		};
		
		int c = JOptionPane.showConfirmDialog(null, inputs, "New Record", JOptionPane.PLAIN_MESSAGE);
		
		String recordPath = System.getProperty("user.home")+File.separator+recordFileName.getText()+".xml"; 
		
		controller.setRecordsLocation(recordPath);
		controller.setPatients(new ArrayList<Patient>());
		controller.save();
		controller.loadRecords();
		
		
		updateGUI();
	}*/
	
	private void newRecordAction() {
		
		controller.actionBrowseSaveFile();
		
		updateGUI();
	}
	
	private void saveAction(){
		
		controller.save();
		
		updateGUI();
	}
	
	private void exitAction(){
		System.exit(0);
	}
	
	private void updateAction(){
		updateStatsGUI();
	}
	
	private void dateChangeAction(){
		updateStatsGUI();
	}
	
	private void versionNotesAction(){
        JTextPane notePane = new JTextPane();
        notePane.setEditable(false);
        notePane.setBackground(getBackground());
        
        StringBuilder str = new StringBuilder();
        str.append(String.format("VER. %s\n\n", Constants.RELEASE_VERSION));
        str.append("RELEASE NOTES\n");
        str.append("-New Feature: Addition of Comments column for notes.\n\n");
        str.append("-Removed: Number of birads 4/5 lost audit category.\n\n");
        str.append("PREVIOUS VERSION NOTES\n");
        str.append("-Bug Fix: Resolved error when editing sorted columns.\n");
        str.append("-Update: Patient names are now editable. Duplicate patient names may be entered.\n");
        str.append("-Update: Relocated add patient function from the 'File' menu item, into the 'Function' menu item.\n");
        str.append("-Update: Addition of version release notes.\n");
        str.append("-Update: Additional audit stats.\n\n");
        str.append("KNOWN ISSUES \n");
        str.append("-Clicking cancel on Load Record menu item automatically reloads the default record\n\n");
        str.append("PLANNED UPDATES \n");
        str.append("-Update audit statistics.\n");
        str.append("-Export statistics to documents for submissions.\n");
        str.append("-Add backup and preventative data-loss protection");
        notePane.setText(str.toString());
        JOptionPane.showMessageDialog(null, notePane, "Release Notes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void auditNotesAction(){
        JTextPane notePane = new JTextPane();
        notePane.setEditable(false);
        notePane.setBackground(getBackground());
        
        StringBuilder str = new StringBuilder();
        str.append("AUDIT NOTES\n\n");
        str.append("# of mammogram cases: \t\t\t Number of patient case entries. \n");
        str.append("# of mammogram cases BI-RADS 0,4,5: \t Sum of cases where diagnostic category is 0, 4, or 5. \n");
        str.append("# of biopsies performed: \t\t\t Sum of cases where biopsy is not 'N/A'. \n");
        str.append("# of BI-RADS category 4: \t\t\t Sum of cases where diagnostic category is 4. \n");
        str.append("# of BI-RADS category 5: \t\t\t Sum of cases where diagnostic category is 5. \n");
        str.append("# of biopsies that were malignant: \t\t Sum of cases where biopsy result is 0 - 3. \n");
        str.append("# of BI-RADS category 4 that were malignant: \t Sum of cases where diagnostic category is 4 and biopsy result is 0 - 3. \n");
        str.append("# of BI-RADS category 5 that were malignant: \t Sum of cases where diagnostic category is 5 and biopsy result is 0 - 3. \n");
        str.append("# of biopsies that were benign: \t\t Sum of cases where biopsy result is 4 - 8. \n");
        str.append("# of BI-RADS 4 and 5 that were lost: \t\t Sum of cases where diagnostic category is 4 or 5 and biopsy result is 'N/A'. \n");
        str.append("# of ductal carcinoma in situ: \t\t Sum of cases where biopsy result is '0:DCIS'. \n");
        str.append("# of invasive ductal or invasive lobular: \t\t ----. \n");
        str.append("# of invasive ducatl or lobular w/ axillary: \t ----. \n");
        str.append("# of true positives: \t\t\t Sum of cases where diagnostic category is 4 or 5 and biopsy result is 0 - 3. \n");
        str.append("# of false positives: \t\t\t Sum of cases where diagnostic category is 4 or 5 and biopsy result is 4 - 8. \n");
        str.append("% of positive predictive value (BI-RADS 4): \t Sum of malignant category 4 cases divided by sum of all category 4 cases. \n");
        str.append("% of positive predictive value (BI-RADS 5): \t Sum of malignant category 5 cases divided by sum of all category 5 cases. \n");
        str.append("% of positive predictive value (BI-RADS 4 and 5): \t Sum of malignant category 4 and 5 cases divided by sum of all category 4 and 5 cases. \n");
        str.append("% of cancer detection rate: \t\t Sum of malignant biopsies divided by all cases. \n");
        str.append("% recall rate: \t\t\t Sum of cases where neither diagnostic nor ultrasound is 'N/A' divided by the total sum of cases. \n");
        notePane.setText(str.toString());
        JOptionPane.showMessageDialog(null, notePane, "How Audit Pathology Summaries are Calculated", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addProviderAction() {
    	
    	JTextField providerNameText = new JTextField();
    	
    	final JComponent[] inputs = new JComponent[] {
    			new JLabel("New Provider Name"),
    			providerNameText
    	};
    	
    	JOptionPane.showConfirmDialog(null, inputs, "New Provider", JOptionPane.PLAIN_MESSAGE);
    	
    	String providerName = providerNameText.getText();
    	String cleanProviderName = providerName.replaceAll(" ", "_");
    	String recordFilePath = System.getProperty("user.home")+File.separator+cleanProviderName+".xml"; 
    	
    	Provider provider = new Provider(providerName, recordFilePath);
    	
    	controller.addProvider(provider);
    	controller.loadProviders();
    	
    	providerBox.addItem(provider);
		providerBox.repaint();
    	
    }
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CategoryView window = new CategoryView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new MenuFrame();
		frame.setBounds(100, 100, 100, 100);
		frame.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * Default record loaded
		 */
			String recordPath = System.getProperty("user.home")+File.separator+"default_record.xml"; 
			controller.setRecordsLocation(recordPath);
			controller.loadRecords();
			controller.loadProviders();
			patientTableModel.getRows().addAll(controller.getPatients());
			
			statsTableModel.getRows().addAll(controller.getPatients());
			statsTableModel.updateTable();
			
			auditTableModel.getRows().addAll(controller.getPatients());
			auditTableModel.updateTable();
			
//			updateGUI();
			
		/*
		 * 
		 */
			
		JPanel statPanel = new JPanel();
		
		JPanel outcomePanel = new JPanel();
		outcomePanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK),
                "Patient Outcome Data",
                TitledBorder.LEFT,
                TitledBorder.TOP));
		JTable statsTable = new JTable(statsTableModel);
		statsTable.setFocusable(false);
		statsTable.setRowSelectionAllowed(false);
		statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		statsTable.setOpaque(false);
		((DefaultTableCellRenderer)statsTable.getDefaultRenderer(Object.class)).setOpaque(false);
		statsTable.getColumnModel().getColumn(1).setMinWidth(200);
		statsTable.getColumnModel().getColumn(1).setMaxWidth(200);
		statsTable.setPreferredScrollableViewportSize(new Dimension(600,(int)statsTable.getPreferredSize().getHeight()));
		statsTable.setFillsViewportHeight(true);

		outcomePanel.setMaximumSize(new Dimension(650, (int)statsTable.getPreferredSize().getHeight()));
		outcomePanel.add(new JScrollPane(statsTable));
		
		JPanel auditPanel = new JPanel();
		auditPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK),
                "Audit Pathology Summary",
                TitledBorder.LEFT,
                TitledBorder.TOP));
		JTable auditTable = new JTable(auditTableModel);
		auditTable.setFocusable(false);
		auditTable.setRowSelectionAllowed(false);
		auditTable.setOpaque(false);
		((DefaultTableCellRenderer)auditTable.getDefaultRenderer(Object.class)).setOpaque(false);
		auditTable.getColumnModel().getColumn(0).setMinWidth(50);
		auditTable.getColumnModel().getColumn(0).setMaxWidth(50);
		auditTable.setPreferredScrollableViewportSize(new Dimension(600,(int)auditTable.getPreferredSize().getHeight()));
		auditTable.setFillsViewportHeight(true);
		auditTable.setShowGrid(false);
		auditPanel.add(new JScrollPane(auditTable));
		
		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
		datePanel.setMaximumSize(new Dimension(650, 100));
		datePanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.BLACK),
                "[Select Date Range]",
                TitledBorder.CENTER,
                TitledBorder.TOP));
		UtilDateModel model1 = new UtilDateModel();
		JDatePanelImpl datePanel1 = new JDatePanelImpl(model1);
		startDate = new JDatePickerImpl(datePanel1);
		UtilDateModel model2 = new UtilDateModel();
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
		endDate = new JDatePickerImpl(datePanel2);
		
		startDate.setMaximumSize(new Dimension(100, (int)startDate.getPreferredSize().getHeight()));
		endDate.setMaximumSize(new Dimension(100, (int)endDate.getPreferredSize().getHeight()));
		
		datePanel.add(startDate);
		datePanel.add(new JPanel());
		datePanel.add(endDate);
		
		ActionListener handler = new ActionHandler();
		startDate.addActionListener(handler);
		endDate.addActionListener(handler);
		
		statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
		statPanel.add(datePanel);
		statPanel.add(outcomePanel);
		statPanel.add(auditPanel);
		
		JScrollPane tablePane = new JScrollPane(patientTable);
		
		JToolBar toolBar = new JToolBar();
		
//		ImageIcon addProviderIcon = new ImageIcon(cl.getResource("images/icon_delete_sm_02.png"));
//		JButton addProviderBtn = new JButton(addProviderIcon);
		JButton addProviderBtn = new JButton("+");
		addProviderBtn.setActionCommand(ADD_PROVIDER_ACTION);
		addProviderBtn.setToolTipText("Add new provider");
		addProviderBtn.addActionListener(handler);
		
		List<Provider> providers = controller.getProviders();
		
		JComboBox<Provider> providerBox = new JComboBox<Provider>();
		providerBox.setActionCommand(CHANGE_PROVIDER_ACTION);
		providerBox.setToolTipText("Select a provider");
		providerBox.addActionListener(handler);
		providerBox.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  controller.setRecordsLocation(((Provider)providerBox.getSelectedItem()).getRecordFile());
		    	  controller.setPatients(new ArrayList<Patient>());
		    	  controller.loadRecords();
		    	  updateGUI();
		      }
		    });
		
		for(Provider provider : providers) {
			providerBox.addItem(provider);
		}
		
		toolBar.add(providerBox);
		toolBar.add(addProviderBtn);
		
		this.providerBox = providerBox;
	    
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		frame.getContentPane().add(tablePane, BorderLayout.CENTER);
		frame.getContentPane().add(statPanel, BorderLayout.EAST);
		
	}
	
	private void updatePatientGUI(){
		
		patientTableModel.setRows(controller.getPatients());
		patientTableModel.updateTable();
		
	}
	
	private void updateStatsGUI(){
		
		Date start = retrieveDate(startDate);
		Date end = retrieveDate(endDate);
		
		controller.setDateRange(start, end);
		
		statsTableModel.setRows(controller.limitByDates());
		auditTableModel.setRows(controller.limitByDates());

		
		statsTableModel.updateTable();
		auditTableModel.updateTable();
	}
	
	private void updateGUI(){
		
		updatePatientGUI();
		updateStatsGUI();
		
		/*Date start = retrieveDate(startDate);
		Date end = retrieveDate(endDate);
		
		controller.setDateRange(start, end);
		
		patientTableModel.setRows(controller.getPatients());
		statsTableModel.setRows(controller.limitByDates());
		auditTableModel.setRows(controller.limitByDates());

		
		patientTableModel.updateTable();
		statsTableModel.updateTable();
		auditTableModel.updateTable();*/
	}
	
	private Date retrieveDate(JDatePickerImpl picker){
		
		if(picker.getModel().getValue() == null){
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(picker.getModel().getYear(), picker.getModel().getMonth(), picker.getModel().getDay(), 0, 0, 0);
		return cal.getTime();
		
	}
	
	//handles all the input
	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			
			if(action.equals(ADD_ACTION))
				addAction();
			else if(action.equals(LOAD_ACTION))
				loadAction();
			else if(action.equals(CREATE_ACTION))
				newRecordAction();
			else if(action.equals(SAVE_ACTION))
				saveAction();
			else if(action.equals(EXIT_ACTION))
				exitAction();
			else if(action.equals(DATE_CHANGE_ACTION))
				dateChangeAction();
			else if(action.equals(VERSION_NOTES_ACTION))
				versionNotesAction();
			else if(action.equals(AUDIT_NOTES_ACTION))
				auditNotesAction();
			else if(action.equals(ADD_PROVIDER_ACTION))
				addProviderAction();
		}
	}
	
	private class MenuFrame extends JFrame{
		
		private static final long serialVersionUID = -5022817404550375422L;

		public MenuFrame() {
	        initUI();
		}

	    public final void initUI() {
	
	        JMenuBar menubar = new JMenuBar();
	
	        JMenu file = new JMenu("File");
	        file.setMnemonic(KeyEvent.VK_F);
	        
	        JMenu function = new JMenu("Function");
	        function.setMnemonic(KeyEvent.VK_F);
	        
	        JMenu about = new JMenu("About");
	        about.setMnemonic(KeyEvent.VK_F);
	        
	        ActionListener handler = new ActionHandler();
	
	        JMenuItem exitMenuItem = new JMenuItem("Exit");
	        exitMenuItem.setMnemonic(KeyEvent.VK_E);
	        exitMenuItem.setToolTipText("Exit application");
	        exitMenuItem.setActionCommand(EXIT_ACTION);
	        exitMenuItem.addActionListener(handler);
	        
	        JMenuItem createMenuItem = new JMenuItem("New Record...");
	        createMenuItem.setMnemonic(KeyEvent.VK_E);
	        createMenuItem.setToolTipText("Create new record file");
	        createMenuItem.setActionCommand(CREATE_ACTION);
	        createMenuItem.addActionListener(handler);
	        
	        JMenuItem loadMenuItem = new JMenuItem("Load Record...");
	        loadMenuItem.setMnemonic(KeyEvent.VK_E);
	        loadMenuItem.setToolTipText("Load existing record file");
	        loadMenuItem.setActionCommand(LOAD_ACTION);
	        loadMenuItem.addActionListener(handler);
	        
	        JMenuItem saveMenuItem = new JMenuItem("Save Record...");
	        saveMenuItem.setMnemonic(KeyEvent.VK_E);
	        saveMenuItem.setToolTipText("Save changes to this record file");
	        saveMenuItem.setActionCommand(SAVE_ACTION);
	        saveMenuItem.addActionListener(handler);
	        
	        JMenuItem addMenuItem = new JMenuItem("Add Patient...");
	        addMenuItem.setMnemonic(KeyEvent.VK_E);
	        addMenuItem.setToolTipText("Add new patient to this record file");
	        addMenuItem.setActionCommand(ADD_ACTION);
	        addMenuItem.addActionListener(handler);
	        
	        JMenuItem versionNotesMenuItem = new JMenuItem("Release Notes");
	        versionNotesMenuItem.setMnemonic(KeyEvent.VK_E);
	        versionNotesMenuItem.setToolTipText("Read updates associated with this software release version");
	        versionNotesMenuItem.setActionCommand(VERSION_NOTES_ACTION);
	        versionNotesMenuItem.addActionListener(handler);
	        
	        JMenuItem auditStatsReportMenuItem = new JMenuItem("About Audit Pathologies");
	        auditStatsReportMenuItem.setMnemonic(KeyEvent.VK_E);
	        auditStatsReportMenuItem.setToolTipText("Read about how the pathology summaries are calculated");
	        auditStatsReportMenuItem.setActionCommand(AUDIT_NOTES_ACTION);
	        auditStatsReportMenuItem.addActionListener(handler);
	
//	        file.add(addMenuItem);
	        file.add(createMenuItem);
	        file.add(loadMenuItem);
	        file.add(saveMenuItem);
	        file.add(exitMenuItem);
	        
	        function.add(addMenuItem);
	        
	        about.add(versionNotesMenuItem);
	        about.add(auditStatsReportMenuItem);
	
	        menubar.add(file);
	        menubar.add(function);
	        menubar.add(about);
	
	        setJMenuBar(menubar);
	
	        setTitle(String.format("Imaging Category Tool - Ver. %s", Constants.RELEASE_VERSION));
	        setSize(300, 200);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	    }
	}
	
	private class ComboItem{
	    private Integer value;
	    private String label;

	    public ComboItem(Integer value, String label) {
	        this.value = value;
	        this.label = label;
	    }
	    
	    public ComboItem(Integer value){
	    	this.value = value;
	    	this.label = value.toString();
	    }

	    public Integer getValue() {
	        return this.value;
	    }

	    public String getLabel() {
	        return this.label;
	    }

	    @Override
	    public String toString() {
    		return label;
	    }
	    
	}

}
