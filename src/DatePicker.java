import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DatePicker extends JPanel {

	private Calendar calendar = Calendar.getInstance();

	private JComboBox<Integer> day = new JComboBox<Integer>();
	private JComboBox<Month> month = new JComboBox<Month>();
	private JComboBox<Integer> year = new JComboBox<Integer>();

	private JLabel label = new JLabel();

	private SimpleDateFormat sdf = new SimpleDateFormat("M/d/y h:mm aa");
	private SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm aa");

	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();

	public DatePicker(Date date) {
		this();
		setDate(date);
	}

	public DatePicker() {

		ActionListener dayActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("day start");
				setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), ((Integer)day.getSelectedItem()).intValue(), 0, 0, 0);
				//System.out.println("day end: " + calendar.getTime());
				for (ActionListener actionListener : actionListeners) {
					actionListener.actionPerformed(e);
				}
			}
		};

		ActionListener monthActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("month start");
				setDate(calendar.get(Calendar.YEAR), ((Month)month.getSelectedItem()).getValue() - 1, calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				//System.out.println("month end: " + calendar.getTime());
				for (ActionListener actionListener : actionListeners) {
					actionListener.actionPerformed(e);
				}
			}
		};

		ActionListener yearActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("year start");
				setDate(((Integer)year.getSelectedItem()).intValue(), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				//System.out.println("year end: " + calendar.getTime());
				for (ActionListener actionListener : actionListeners) {
					actionListener.actionPerformed(e);
				}
			}
		};

		setBorder(BorderFactory.createEtchedBorder());

		setTimeofDayToZero();

		for (Month m : Month.values()) {
			month.addItem(m);
		}

		for (int i = 1900; i <= calendar.get(Calendar.YEAR); i++) {
			year.addItem(new Integer(i));
		}

		add(month);
		add(new JLabel("/"));
		add(day);
		add(new JLabel("/"));
		add(year);

		add(label);

		updateDisplay();
		
		day.setFocusable(false);
		month.setFocusable(false);
		year.setFocusable(false);
		
		day.addActionListener(dayActionListener);
		month.addActionListener(monthActionListener);
		year.addActionListener(yearActionListener);
	}

	public void updateDisplay() {

		ActionListener[] dayActionListeners = day.getActionListeners();
		ActionListener[] monthActionListeners = month.getActionListeners();
		ActionListener[] yearActionListeners = year.getActionListeners();

		for (ActionListener actionListener : dayActionListeners) {
			day.removeActionListener(actionListener);
		}
		for (ActionListener actionListener : monthActionListeners) {
			month.removeActionListener(actionListener);
		}
		for (ActionListener actionListener : yearActionListeners) {
			year.removeActionListener(actionListener);
		}

		day.removeAllItems();

		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= daysInMonth; i++) {
			day.addItem(new Integer(i));
		}

		if (!setSelectedItem(day, calendar.get(Calendar.DAY_OF_MONTH))) {
			System.out.println("couldnt find day");
		}

		month.setSelectedItem(Month.of(calendar.get(Calendar.MONTH) + 1));

		if (!setSelectedItem(year, calendar.get(Calendar.YEAR))) {
			System.out.println("couldnt find year: " + calendar.get(Calendar.YEAR));
		}

		label.setText(timeSdf.format(calendar.getTime()));

		for (ActionListener actionListener : dayActionListeners) {
			day.addActionListener(actionListener);
		}
		for (ActionListener actionListener : monthActionListeners) {
			month.addActionListener(actionListener);
		}
		for (ActionListener actionListener : yearActionListeners) {
			year.addActionListener(actionListener);
		}
	}

	public boolean setSelectedItem(JComboBox<Integer> combobox, int itemValue) {
		for (int i = 0; i < combobox.getModel().getSize(); i++) {
			if (combobox.getModel().getElementAt(i).intValue() == itemValue) {
				combobox.setSelectedIndex(i);
				return true;
			}
		}
		return false;
	}

	public void setTimeofDayToZero() {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		label.setText(timeSdf.format(calendar.getTime()));
	}

	public Date getDate() {
		return calendar.getTime();
	}

	public void setDate(int year, int month, int date, int hourOfDay, int minute, int second) {
		calendar.set(year, month, date, hourOfDay, minute, second);
		updateDisplay();
	}

	public void setDate(Date date) {
		calendar.setTime(date);
		updateDisplay();
	}

	public String getFormattedDate() {
		return sdf.format(calendar.getTime());
	}

	public void addActionListener(ActionListener actionListener) {
		actionListeners.add(actionListener);
	}

	public void removeActionListener(ActionListener actionListener) {
		actionListeners.remove(actionListener);
	}

}
