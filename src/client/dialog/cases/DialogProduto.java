package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import server.dao.AbstractDAO;
import server.dao.ProdutoDAO;
import server.entities.Flag;
import server.entities.Produto;
import server.entities.UnidadeMedida;
import server.entities.abstracts.EntityState;
import server.persistence.Persistence;
import arquitetura.client.binding.annotation.TextBindingAnnot;
import arquitetura.client.components.label.EJDLabel;
import arquitetura.client.components.panel.EJDCheckBox;
import arquitetura.client.components.textfield.EJDDecimalField;
import arquitetura.client.components.textfield.EJDTextField;
import arquitetura.client.components.textfield.EnumFiltroTextField;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.dialog.consultas.ConsultaUnidadeMedida;
import client.dialog.util.UtilClient;
import client.table.TableModelEntidade.ITableModelEntidade;

import common.base.FlagBase;
import common.util.UtilCommon;

public class DialogProduto extends DialogCrud<Produto> {
    private static final long serialVersionUID = 1L;

    private final EJDLabel lblIdentificador = new EJDLabel("Identificador:");
    private final EJDLabel lblDescricao = new EJDLabel("Descri\u00E7\u00E3o:");
    private final EJDLabel lblPreco = new EJDLabel("Pre\u00E7o:", true);
    private final JTextField fieldIdentificador = new JTextField();
    private final EJDTextField fieldDescricao = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
    private final EJDDecimalField fieldPreco = new EJDDecimalField(12, 2);
    private final JPanel panelPreco = new JPanel();
    private final EJDCheckBox chboxStatus = new EJDCheckBox("Ativo");

    private final EJDLabel lblUnidadeDeMedida = new EJDLabel("Unidade de Medida:");
    private final JPanel panelUnidadeMedida = new JPanel();

    @TextBindingAnnot(
            obrigatorio = true,
            tamanho = 5, filtro = EnumFiltroTextField.MAIUSCULAS)
    private final EJDTextField fieldSiglaUnidadeMedida = new EJDTextField();

    @TextBindingAnnot(
            obrigatorio = false,
            tamanho = 80)
    private final EJDTextField fieldDescricaoUnidadeMedida = new EJDTextField();
    private final JButton btnUnidadeMedida = new JButton("");

    private UnidadeMedida unidadeMedida = new UnidadeMedida();

    private ConsultaUnidadeMedida consultaUnidadeMedida = new ConsultaUnidadeMedida();

    public DialogProduto(Window window) {
        super(window);

        initComponents();
    }

    private void initComponents() {
        panelPrincipal.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        panelPrincipal.add(this.lblIdentificador, "cell 0 0,alignx trailing");

        panelPrincipal.add(this.fieldIdentificador, "cell 1 0,growx");

        panelPrincipal.add(this.lblDescricao, "cell 0 1,alignx trailing");

        panelPrincipal.add(this.fieldDescricao, "cell 1 1,growx");

        panelPrincipal.add(this.lblPreco, "cell 0 2,alignx trailing");

        panelPrincipal.add(panelPreco, "cell 1 2,grow");
        panelPreco.setLayout(new BorderLayout(0, 0));
        panelPreco.add(fieldPreco, BorderLayout.CENTER);
        panelPreco.add(chboxStatus, BorderLayout.EAST);

        panelPrincipal.add(lblUnidadeDeMedida, "cell 0 3,alignx trailing");

        panelPrincipal.add(panelUnidadeMedida, "cell 1 3,grow");
        panelUnidadeMedida.setLayout(new BorderLayout(0, 0));

//        fieldSiglaUnidadeMedida.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                try {
//                    consultarUnidadeMedida();
//                } catch (Exception e1) {
//                    EJDOptionPane.informWarning(EnumEJDException.NENHUM_REGISTRO_ENCONTRADO);
//                }
//            }
//
//        });

        panelUnidadeMedida.add(fieldSiglaUnidadeMedida, BorderLayout.WEST);

        panelUnidadeMedida.add(fieldDescricaoUnidadeMedida, BorderLayout.CENTER);
        btnUnidadeMedida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarUnidadesMedidas();
            }
        });
        btnUnidadeMedida.setIcon(new ImageIcon(DialogProduto.class.getResource("/img/search.png")));

        panelUnidadeMedida.add(btnUnidadeMedida, BorderLayout.EAST);


        fieldDescricaoUnidadeMedida.setColumns(10);
        fieldDescricaoUnidadeMedida.setFocusable(false);

        fieldSiglaUnidadeMedida.setColumns(10);
        fieldSiglaUnidadeMedida.setFocusable(false);
        marcarCamposObrigatorios(lblUnidadeDeMedida, lblDescricao, lblPreco);
    }

    @Override
    public JComponent getFirstFocusable() {
        return fieldDescricao;
    }

    public static void main(String[] args) throws EJDLogicException {

        try {
            new DialogProduto(null).setVisible(true);

        } finally {
            Persistence.closeAll();
        }
    }

    @Override
    public AbstractDAO<Produto> getDAO() {
        return new ProdutoDAO();
    }

    @Override
    public String getTitulo() {
        return "Produto";
    }

    @Override
    public void preecherCampos(Produto entidade) {
        fieldIdentificador.setText(entidade != null && entidade.getIdProduto() != null ? entidade.getIdProduto().toString() : null);
        fieldDescricao.setText(entidade != null ? entidade.getDescricao() : null);
        fieldPreco.setText(entidade != null ? UtilCommon.formatDecimalSomentePonto(entidade.getValorUnitarioEntrada()) : null);
        if (entidade != null && entidade.getFlagStatus() != null) {
            chboxStatus.setSelected(entidade != null ? (entidade.getFlagStatus().getIdFlag().compareTo(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) == 0 ? true : false) : false);
            unidadeMedida = entidade.getUnidadeMedida();
            fieldSiglaUnidadeMedida.setText(unidadeMedida.getSigla());
            fieldDescricaoUnidadeMedida.setText(unidadeMedida.getDescricao());
        } else {
            fieldSiglaUnidadeMedida.setText("");
            fieldDescricaoUnidadeMedida.setText("");
        }

    }

    @Override
    public void preecherEntidade(Produto entidade) throws EJDLogicException {
        entidade.setUnidadeMedida(unidadeMedida);
        entidade.setDescricao(fieldDescricao.getText());
        BigDecimal preco = UtilCommon.convertBigDecimalSomentePonto(fieldPreco.getText());
        entidade.setValorUnitarioEntrada(preco);
        entidade.setEstoque(new BigDecimal(0));
        entidade.setFlagStatus(chboxStatus.isSelected() ? new Flag(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) : new Flag(FlagBase.PRODUTO_IDFLAGSTATUS_BAIXADO));
        validarCamposObrigatorios(entidade);
    }

    @Override
    public void habilitarCampos(EntityState state) {
        boolean habilita = false;
        switch (state) {
        case DELETED:
        case UNMODIFIED:
            habilita = false;
            break;
        case NEW:
        case MODIFIED:
            habilita = true;
            break;
        }

        fieldIdentificador.setEnabled(false);
        fieldDescricao.setEnabled(habilita);
        fieldPreco.setEnabled(habilita);
        chboxStatus.setEnabled(habilita);
        fieldSiglaUnidadeMedida.setEnabled(false);
        fieldDescricaoUnidadeMedida.setEnabled(false);
        btnUnidadeMedida.setEnabled(habilita);

    }

    @Override
    public ITableModelEntidade<Produto> getTableModelEntidade() {

        return new ITableModelEntidade<Produto>() {
            @Override
            public Object getCampoEntidade(Produto entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getIdProduto();
                case 1:
                    return entidade.getDescricao();
                case 2:
                    return UtilCommon.formatDecimal(entidade.getValorUnitarioEntrada());
                case 3:
                    dao.sincronizar(entidade);
                    return entidade.getFlagStatus().getDescricao();
                }
                return null;
            }

            @Override
            public List<Produto> getEntidades() {
                return dao.localizarTodos();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Identificador", "Descrição", "Preço", "Status" };
            }

        };
    }

    @Override
    public void validarCamposObrigatorios(Produto entidade) throws EJDLogicException {
        ValidateEntity validateEntity = new ValidateEntity();

        validateEntity.validateNotNull(entidade.getDescricao(), "Descrição");
        validateEntity.validateNotNull(entidade.getValorUnitarioEntrada(), "Preço");
        validateEntity.doValidate();

    }

    protected void consultarUnidadesMedidas() {

        try {
            setCursor(UtilClient.CURSOR_WAIT);

            DialogCrud<UnidadeMedida> dialogUnidadeMedida = new DialogUnidadeMedida(getOwner());

            unidadeMedida = consultaUnidadeMedida.consultarUnidadesMedidas(dialogUnidadeMedida);

            if (unidadeMedida != null) {
                fieldSiglaUnidadeMedida.setText(unidadeMedida.getSigla());
                fieldDescricaoUnidadeMedida.setText(unidadeMedida.getDescricao());
            }

        } catch (Exception e) {
            reportException(e);
        } finally {
            setCursor(UtilClient.CURSOR_DEFAULT);
        }
    }

    @SuppressWarnings("unused")
    private void consultarUnidadeMedida() throws Exception  {

        unidadeMedida = consultaUnidadeMedida.consultarUnidadeMedida(fieldSiglaUnidadeMedida.getText().toUpperCase().trim());

        if (unidadeMedida != null){
            fieldDescricaoUnidadeMedida.setText(unidadeMedida.getDescricao());
            fieldSiglaUnidadeMedida.setText(unidadeMedida.getSigla());
        } else {
            throw new EJDLogicException(EnumEJDException.NENHUM_REGISTRO_ENCONTRADO);
        }

    }

    private void marcarCamposObrigatorios(EJDLabel... labels){

        for (EJDLabel ejdLabel : labels) {
            ejdLabel = ejdLabel.marcarCamposObrigatorios(true);

        }

    }

}
