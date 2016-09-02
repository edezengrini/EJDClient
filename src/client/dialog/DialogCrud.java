package client.dialog;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import server.dao.AbstractDAO;
import server.entities.abstracts.AbstractEntity;
import server.entities.abstracts.EntityState;
import arquitetura.common.exception.EJDLogicException;
import client.dialog.util.ReportException;
import client.dialog.util.UtilClient;
import client.table.TableModelEntidade.ITableModelEntidade;

public abstract class DialogCrud<E extends AbstractEntity> extends DialogPadrao {

	private static final long serialVersionUID = 1L;

	private EntityState entityState = EntityState.UNMODIFIED;

	private E entidadeCorrente;

	private final JPanel panelBar = new JPanel();
	private final JButton buttonInserir = new JButton("Novo");
	private final JButton buttonEditar = new JButton("Editar");
	private final JButton buttonExcluir = new JButton("Excluir");
	private final JButton buttonSalvar = new JButton("Salvar");
	private final JButton buttonCancelar = new JButton("Cancelar");

	protected JPanel panelStatus = new JPanel(new BorderLayout());
	protected JLabel labelStatusWeast = new JLabel(" Status");

	private final JButton buttonLocalizar = new JButton("Localizar");

	protected final AbstractDAO<E> dao = getDAO();

	public DialogCrud(Window window) {
		super(window);
		initComponents();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				atualizar();
			}
		});
	}

	public abstract AbstractDAO<E> getDAO();
	
	public abstract void preecherEntidade(E entidade) throws EJDLogicException;

	public abstract void preecherCampos(E entidade);
	
	public abstract void habilitarCampos(EntityState state);

	public abstract ITableModelEntidade<E> getTableModelEntidade();

	public abstract void validarCamposObrigatorios(E entidade) throws EJDLogicException;

	private void initComponents() {

		add(panelStatus, BorderLayout.SOUTH);
		panelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		panelStatus.add(labelStatusWeast, BorderLayout.WEST);

		this.buttonLocalizar.setMargin(new Insets(2, 4, 2, 4));
		this.buttonLocalizar.setMnemonic('L');
		this.buttonLocalizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				localizar();
			}
		});
		this.panelComando.add(this.buttonLocalizar);

		this.panelCenter.add(this.panelBar, BorderLayout.SOUTH);
		this.panelBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.panelBar.setLayout(new GridLayout(1, 5, 2, 2));
		this.buttonInserir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inserir();
			}
		});
		this.buttonInserir.setMargin(new Insets(2, 2, 2, 2));
		this.buttonInserir.setMnemonic('N');

		this.panelBar.add(this.buttonInserir);
		this.buttonEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editar();
			}
		});
		this.buttonEditar.setMargin(new Insets(2, 2, 2, 2));
		this.buttonEditar.setMnemonic('E');

		this.panelBar.add(this.buttonEditar);
		this.buttonExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluir();
			}
		});
		this.buttonExcluir.setMargin(new Insets(2, 2, 2, 2));
		this.buttonExcluir.setMnemonic('E');

		this.panelBar.add(this.buttonExcluir);
		this.buttonSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		this.buttonSalvar.setMargin(new Insets(2, 2, 2, 2));
		this.buttonSalvar.setMnemonic('S');

		this.panelBar.add(this.buttonSalvar);
		this.buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});
		this.buttonCancelar.setMargin(new Insets(2, 2, 2, 2));
		this.buttonCancelar.setMnemonic('C');

		this.panelBar.add(this.buttonCancelar);

		this.setModal(true);

        possibilitarAvancarEnterComoTab();

        possibilitarVoltarEnterComoTab();

	}

    private void possibilitarVoltarEnterComoTab() {
        Set<AWTKeyStroke> bKeys = new HashSet<AWTKeyStroke>(KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        bKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, bKeys);
    }

    private void possibilitarAvancarEnterComoTab() {
        Set<AWTKeyStroke> fkeys = new HashSet<AWTKeyStroke>(KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        fkeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, fkeys);
    }

	public boolean isEdicao() {
		if (entityState != EntityState.UNMODIFIED) {
			JOptionPane.showMessageDialog(this, "Salve ou Cancele antes de Continuar!");
			return true;
		}
		return false;
	}

	public boolean isEdicaoEntidade() {
		if (entityState != EntityState.UNMODIFIED) {
			return true;
		}
		return false;
	}

	protected void localizar() {
		if (isEdicao()) {
			return;
		}

		try {
			setCursor(UtilClient.CURSOR_WAIT);
			DialogLocalizar<E> formLocalizar = new DialogLocalizar<E>(this, dao.localizarTodos());
			formLocalizar.setVisible(true);

			E entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

			if (entidadeSelecionada != null) {
				entidadeCorrente = entidadeSelecionada;
			}
			atualizar();
		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

	protected void localizarEntidade(AbstractEntity entidade) {
		if (isEdicao()) {
			return;
		}

		try {
			setCursor(UtilClient.CURSOR_WAIT);
			DialogLocalizar<E> formLocalizar = new DialogLocalizar<E>(this, dao.localizarTodos());
			formLocalizar.setVisible(true);

			E entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

			if (entidadeSelecionada != null) {
				entidadeCorrente = entidadeSelecionada;
			}
			atualizar();
		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

	private void editar() {
		if (entidadeCorrente == null || entidadeCorrente.getId() == null) {
			JOptionPane.showMessageDialog(this, "Nenhum registro Selecionado.");
			return;
		}

		try {
			setCursor(UtilClient.CURSOR_WAIT);
			entityState = EntityState.MODIFIED;
			labelStatusWeast.setText(" Alterando Registro");

			atualizar();

			sendFocusToFirst();

		} catch (Exception e) {
			entityState = EntityState.UNMODIFIED;
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

	public abstract JComponent getFirstFocusable();

	protected void sendFocusToFirst() {
		JComponent component = getFirstFocusable();
		if (component != null) {
			component.requestFocusInWindow();
		}
	}

	// insert
	private void inserir() {

		try {
			setCursor(UtilClient.CURSOR_WAIT);

			entityState = EntityState.NEW;
			entidadeCorrente = dao.criar();

			atualizar();

			labelStatusWeast.setText(" Inserindo Registro");

			sendFocusToFirst();

		} catch (Exception e) {
			entityState = EntityState.UNMODIFIED;
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}

	}

	// delete
	private void excluir() {

		if (entidadeCorrente == null || entidadeCorrente.getId() == null) {
			JOptionPane.showMessageDialog(this, "Nenhum registro Selecionado.");
			return;
		}

		if (JOptionPane.showConfirmDialog(this.getRootPane().getParent(), "Tem certeza que deseja EXCLUIR o registro atual?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
			return;
		}

		try {
			setCursor(UtilClient.CURSOR_WAIT);

			dao.excluir(entidadeCorrente);

			entityState = EntityState.UNMODIFIED;
			entidadeCorrente = null;

			labelStatusWeast.setText(" Registro excluído com sucesso");

			atualizar();

			buttonInserir.requestFocus();

		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

	// save
	private E salvar() {
		try {
			setCursor(UtilClient.CURSOR_WAIT);
			preecherEntidade(entidadeCorrente);
			entidadeCorrente.validate();

			switch (entityState) {
			case NEW:

				entidadeCorrente = dao.inserir(entidadeCorrente);
				break;

			case MODIFIED:

				entidadeCorrente = dao.editar(entidadeCorrente);
				break;

			default:
				break;

			}

			entityState = EntityState.UNMODIFIED;

			atualizar();
			labelStatusWeast.setText(" Registro salvo com sucesso");

			buttonInserir.requestFocus();

		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		} 
		return entidadeCorrente;
	}
	
	// cancel
	private void cancelar() {
		try {
			setCursor(UtilClient.CURSOR_WAIT);

			if (entityState == EntityState.NEW) {
				if (entidadeCorrente != null && entidadeCorrente.getId() != null) {
					entidadeCorrente = dao.localizar(entidadeCorrente.getId());
				} else {
					entidadeCorrente = null;
				}
			}

			entityState = EntityState.UNMODIFIED;

			atualizar();

			labelStatusWeast.setText(" Cancelado");

			buttonInserir.requestFocus();

		} catch (Exception e) {
			reportException(e);
		} finally {
			setCursor(UtilClient.CURSOR_DEFAULT);
		}
	}

	public void reportException(Exception e) {
		ReportException.report(this, e);
	}

	@Override
	public void dispose() {
		if (isEdicao()) {
			return;
		}

		super.dispose();
	}

	public EntityState getStateEntity() {
		return entityState;
	}

	public E getEntidadeCorrente() {
		return entidadeCorrente;
	}

	public void atualizarEntidade(){
		atualizar();
	}

	private void atualizar() {
		if (entidadeCorrente != null) {

			switch (entityState) {
				case NEW:
					buttonInserir.setEnabled(false);
					buttonSalvar.setEnabled(true);
					buttonCancelar.setEnabled(true);
					buttonEditar.setEnabled(false);
					buttonExcluir.setEnabled(false);
					break;
				case DELETED:
				case MODIFIED:
					buttonInserir.setEnabled(false);
					buttonSalvar.setEnabled(true);
					buttonCancelar.setEnabled(true);
					buttonEditar.setEnabled(false);
					buttonExcluir.setEnabled(false);
					break;
				case UNMODIFIED:
					buttonInserir.setEnabled(true);
					buttonSalvar.setEnabled(false);
					buttonCancelar.setEnabled(false);
					buttonEditar.setEnabled(true);
					buttonExcluir.setEnabled(true);
					break;
			}

		} else {
			buttonInserir.setEnabled(true);
			buttonSalvar.setEnabled(false);
			buttonCancelar.setEnabled(false);
			buttonEditar.setEnabled(false);
			buttonExcluir.setEnabled(false);
		}

		buttonLocalizar.setEnabled(entityState == EntityState.UNMODIFIED);
		btnSair.setEnabled(entityState == EntityState.UNMODIFIED);

		preecherCampos(entidadeCorrente);
		habilitarCampos(entityState);

		
	}

}
