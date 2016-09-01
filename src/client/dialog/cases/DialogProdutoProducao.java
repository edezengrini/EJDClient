package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import server.dao.AbstractDAO;
import server.dao.ProdutoDAO;
import server.ejb.ProdutoEJB;
import server.entities.Flag;
import server.entities.Grupo;
import server.entities.Produto;
import server.entities.UnidadeMedida;
import server.entities.abstracts.EntityState;
import arquitetura.client.binding.annotation.DecimalTextBindingAnnot;
import arquitetura.client.components.panel.EJDCheckBox;
import arquitetura.client.components.panel.EJDPanelCodigoDescricao;
import arquitetura.client.components.textfield.EJDDateTimeField;
import arquitetura.client.components.textfield.EJDDecimalField;
import arquitetura.client.components.textfield.EJDTextField;
import arquitetura.client.components.textfield.EnumFiltroTextField;
import arquitetura.client.components.textfield.EnumMascarasDatas;
import arquitetura.common.beans.attribute.Attribute.UPDATABLE_TYPE;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.DateUtil;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.dialog.consultas.ConsultaGrupo;
import client.dialog.consultas.ConsultaUnidadeMedida;
import client.dialog.util.UtilClient;
import client.enums.EnumImage;
import client.layout.VerticalFlowLayout;
import client.table.TableModelEntidade.ITableModelEntidade;

import common.base.FlagBase;
import common.util.UtilCommon;

public class DialogProdutoProducao extends DialogCrud<Produto>{

    private static final long serialVersionUID = 1L;
    private EJDPanelCodigoDescricao panelCodigoDescricao;
    private JPanel panelEstoque;
    private JPanel panelUnidadeMedida;
    private JPanel panelGrupo;
    private JPanel panelEstoqueAtual;
    private JPanel panelEstoqueMinimo;
    private JPanel panelEstoqueMaximo;
    private JPanel panelValor;
    private JPanel panelUnitarioEntrada;
    private JPanel panelUnitarioSaida;
    private JPanel panelCodigoBarra;
    private JPanel panelDataUltimaCompra;
    private JPanel panelStatus;

    private EJDTextField fieldCodigo;
    private EJDTextField fieldDescricao;
    private EJDTextField fieldSiglaUnidadeMedida;
    private EJDTextField fieldDescricaoUnidadeMedida;
    private EJDTextField fieldCodigoGrupo;
    private EJDTextField fieldDescricaoGrupo;
    private EJDTextField fieldCodigoBarra;
    private EJDDateTimeField fieldDataUltimaCompra;
    private EJDDecimalField fieldEstoqueAtual;
    @DecimalTextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanhoInteiro = 10,
            tamanhoDecimal = 4)
    private EJDDecimalField fieldEstoqueMinimo;
    @DecimalTextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanhoInteiro = 10,
            tamanhoDecimal = 4)
    private EJDDecimalField fieldEstoqueMaximo;
    @DecimalTextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanhoInteiro = 10,
            tamanhoDecimal = 4)
    private EJDDecimalField fieldUnitarioSaida;
    @DecimalTextBindingAnnot(
            obrigatorio = true,
            alteravel = UPDATABLE_TYPE.WHILE_NEW,
            tamanhoInteiro = 10,
            tamanhoDecimal = 4)
    private EJDDecimalField fieldUnitarioEntrada;
    private JButton btnUnidadeMedida;
    private JButton btnGrupo;
    private EJDCheckBox chkStatus;

    private UnidadeMedida unidadeMedida = new UnidadeMedida();

    private ConsultaUnidadeMedida consultaUnidadeMedida = new ConsultaUnidadeMedida();

    private Grupo grupo = new Grupo();

    private ConsultaGrupo consultaGrupo = new ConsultaGrupo();

    private ProdutoEJB produtoEJB = new ProdutoEJB();

    public DialogProdutoProducao(Window window) {
        super(window);
        initComponents();
    }

    private void initComponents() {
        panelPrincipal.setLayout(new VerticalFlowLayout());

        panelCodigoDescricao = new EJDPanelCodigoDescricao();
        panelEstoque = new JPanel();
        panelUnidadeMedida = new JPanel();
        panelUnidadeMedida.setBorder(new TitledBorder(null, "Unidade de Medida", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelGrupo = new JPanel();
        panelGrupo.setBorder(new TitledBorder(null, "Grupo", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        panelPrincipal.add(panelCodigoDescricao);

        fieldCodigo = new EJDTextField(EnumFiltroTextField.NUMEROS);
        panelCodigoDescricao.getPanelCodigo().add(fieldCodigo);
        fieldCodigo.setEnabled(false);
        fieldDescricao = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
        panelCodigoDescricao.getPanelDescricao().add(fieldDescricao);

        panelPrincipal.add(panelEstoque);
        panelEstoque.setLayout(new GridLayout(1, 0, 0, 0));

        panelEstoqueAtual = new JPanel();
        panelEstoqueAtual.setBorder(new TitledBorder(null, "Estoque Atual", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelEstoqueAtual);
        panelEstoqueAtual.setLayout(new BorderLayout(0, 0));

        fieldEstoqueAtual = new EJDDecimalField(12,4);
        panelEstoqueAtual.add(fieldEstoqueAtual);

        panelEstoqueMinimo = new JPanel();
        panelEstoqueMinimo.setBorder(new TitledBorder(null, "Estoque M\u00EDnimo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelEstoqueMinimo);
        panelEstoqueMinimo.setLayout(new BorderLayout(0, 0));

        fieldEstoqueMinimo = new EJDDecimalField(10,4);
        panelEstoqueMinimo.add(fieldEstoqueMinimo);

        panelEstoqueMaximo = new JPanel();
        panelEstoqueMaximo.setBorder(new TitledBorder(null, "Estoque M\u00E1ximo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelEstoqueMaximo);
        panelEstoqueMaximo.setLayout(new BorderLayout(0, 0));

        fieldEstoqueMaximo = new EJDDecimalField(10,4);
        panelEstoqueMaximo.add(fieldEstoqueMaximo);

        panelStatus = new JPanel();
        panelStatus.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelEstoque.add(panelStatus);
        panelStatus.setLayout(new BorderLayout(0, 0));

        chkStatus = new EJDCheckBox("Ativo");
        chkStatus.setHorizontalAlignment(SwingConstants.CENTER);
        panelStatus.add(chkStatus);

        panelPrincipal.add(panelUnidadeMedida);
        panelUnidadeMedida.setLayout(new BorderLayout(0, 0));

        fieldSiglaUnidadeMedida = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
        panelUnidadeMedida.add(fieldSiglaUnidadeMedida, BorderLayout.WEST);

        fieldDescricaoUnidadeMedida = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
        panelUnidadeMedida.add(fieldDescricaoUnidadeMedida, BorderLayout.CENTER);

        btnUnidadeMedida = new JButton("");
        btnUnidadeMedida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                consultarUnidadeMedida();
            }
        });
        btnUnidadeMedida.setIcon(new ImageIcon(DialogProdutoProducao.class.getResource(EnumImage.LOCALIZAR.getCaminho())));
        panelUnidadeMedida.add(btnUnidadeMedida, BorderLayout.EAST);

        panelPrincipal.add(panelGrupo);
        panelGrupo.setLayout(new BorderLayout(0, 0));

        fieldCodigoGrupo = new EJDTextField(EnumFiltroTextField.NUMEROS);
        panelGrupo.add(fieldCodigoGrupo, BorderLayout.WEST);

        fieldDescricaoGrupo = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);
        panelGrupo.add(fieldDescricaoGrupo, BorderLayout.CENTER);

        btnGrupo = new JButton("");
        btnGrupo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                consultarGrupo();
            }
        });
        btnGrupo.setIcon(new ImageIcon(DialogProdutoProducao.class.getResource(EnumImage.LOCALIZAR.getCaminho())));
        panelGrupo.add(btnGrupo, BorderLayout.EAST);

        panelValor = new JPanel();
        panelValor.setBorder(null);
        panelPrincipal.add(panelValor);
        panelValor.setLayout(new GridLayout(1, 0, 0, 0));

        panelUnitarioEntrada = new JPanel();
        panelUnitarioEntrada.setBorder(new TitledBorder(null, "Valor Unit\u00E1rio de Entrada", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelValor.add(panelUnitarioEntrada);
        panelUnitarioEntrada.setLayout(new BorderLayout(0, 0));

        fieldUnitarioEntrada = new EJDDecimalField(10,4);
        panelUnitarioEntrada.add(fieldUnitarioEntrada);

        panelUnitarioSaida = new JPanel();
        panelUnitarioSaida.setBorder(new TitledBorder(null, "Valor Unit\u00E1rio de Saida", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelValor.add(panelUnitarioSaida);
        panelUnitarioSaida.setLayout(new BorderLayout(0, 0));

        fieldUnitarioSaida = new EJDDecimalField(10,4);
        panelUnitarioSaida.add(fieldUnitarioSaida);

        panelCodigoBarra = new JPanel();
        panelCodigoBarra.setBorder(new TitledBorder(null, "C\u00F3digo de Barras", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelValor.add(panelCodigoBarra);
        panelCodigoBarra.setLayout(new BorderLayout(0, 0));

        fieldCodigoBarra = new EJDTextField(EnumFiltroTextField.NUMEROS);
        panelCodigoBarra.add(fieldCodigoBarra);

        panelDataUltimaCompra = new JPanel();
        panelDataUltimaCompra.setBorder(new TitledBorder(null, "\u00DAltima Compra", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelValor.add(panelDataUltimaCompra);
        panelDataUltimaCompra.setLayout(new BorderLayout(0, 0));

        fieldDataUltimaCompra = new EJDDateTimeField(EnumMascarasDatas.DDMMYYYY);
        panelDataUltimaCompra.add(fieldDataUltimaCompra);

    }

    protected void consultarGrupo() {
        try {
            setCursor(UtilClient.CURSOR_WAIT);

            DialogCrud<Grupo> dialogGrupo = new DialogGrupo(getOwner());

            grupo = consultaGrupo.consultarGrupos(dialogGrupo);

            if (grupo != null) {
                fieldCodigoGrupo.setText(UtilCommon.toStringNotNull(grupo.getCodigo()));
                fieldDescricaoGrupo.setText(grupo.getDescricao());
            }

        } catch (Exception e) {
            reportException(e);
        } finally {
            setCursor(UtilClient.CURSOR_DEFAULT);
        }
    }

    protected void consultarUnidadeMedida() {
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

    @Override
    public AbstractDAO<Produto> getDAO() {
        return new ProdutoDAO();
    }

    @Override
    public void preecherEntidade(Produto entidade) throws EJDLogicException {
        entidade.setCodigo(produtoEJB.findMaiorIdentificador() != null ? produtoEJB.findMaiorIdentificador().getIdProduto() + 1 : 1);
        entidade.setDescricao(fieldDescricao.getText());
        entidade.setFlagStatus(chkStatus.isSelected() ? new Flag(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) : new Flag(FlagBase.PRODUTO_IDFLAGSTATUS_BAIXADO));
        entidade.setEstoque(new BigDecimal(0));
        entidade.setEstoqueMinimo(UtilCommon.convertBigDecimalSomentePonto(fieldEstoqueMinimo.getText()));
        entidade.setEstoqueMaximo(UtilCommon.convertBigDecimalSomentePonto(fieldEstoqueMaximo.getText()));
        entidade.setValorUnitarioEntrada(UtilCommon.convertBigDecimalSomentePonto(fieldUnitarioEntrada.getText()));
        entidade.setValorUnitarioSaida(UtilCommon.convertBigDecimalSomentePonto(fieldUnitarioSaida.getText()));
        entidade.setCodigoBarra(fieldCodigoBarra.getText());

        DateUtil dataUtil = new DateUtil();
        entidade.setDataUltimaCompra(fieldDataUltimaCompra.getText().isEmpty() || fieldDataUltimaCompra.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(fieldDataUltimaCompra.getText()));

        entidade.setGrupo(grupo);
        entidade.setUnidadeMedida(unidadeMedida);
        validarCamposObrigatorios(entidade);
        produtoEJB.validarEstoque(entidade);
        produtoEJB.validarValor(entidade);
    }

    @Override
    public void preecherCampos(Produto entidade) {
        if (entidade != null){
            fieldCodigo.setText(entidade.getCodigo() != null ? entidade.getCodigo().toString() : null);
            fieldDescricao.setText(entidade.getDescricao());
            fieldUnitarioEntrada.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioEntrada()));
            fieldUnitarioSaida.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioSaida()));
            fieldEstoqueAtual.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getEstoque()));
            fieldEstoqueMinimo.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getEstoqueMinimo()));
            fieldEstoqueMaximo.setText(UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getEstoqueMaximo()));
            fieldCodigoBarra.setText(entidade.getCodigoBarra());
            if (entidade.getFlagStatus() != null) {
                chkStatus.setSelected(entidade != null ? (entidade.getFlagStatus().getIdFlag().compareTo(FlagBase.PRODUTO_IDFLAGSTATUS_ATIVO) == 0 ? true : false) : false);
                unidadeMedida = entidade.getUnidadeMedida();
                fieldSiglaUnidadeMedida.setText(unidadeMedida != null ? unidadeMedida.getSigla() : null);
                fieldDescricaoUnidadeMedida.setText(unidadeMedida != null ? unidadeMedida.getDescricao() : null);
                grupo = entidade.getGrupo();
                fieldCodigoGrupo.setText(grupo != null ? grupo.getCodigo().toString() : null);
                fieldDescricaoGrupo.setText(grupo != null ? grupo.getDescricao() : null);
            } else {
                fieldSiglaUnidadeMedida.setText("");
                fieldDescricaoUnidadeMedida.setText("");
                fieldCodigoGrupo.setText("");
                fieldDescricaoGrupo.setText("");
            }
        }
    }

    @Override
    public void habilitarCampos(EntityState state) {
        boolean habilita = false;
        boolean ativo = chkStatus.isSelected();
        switch (state) {
        case DELETED:
        case UNMODIFIED:
            habilita = false;
            break;
        case NEW:
            ativo = true;
            habilita = true;
            break;
        case MODIFIED:
            ativo = chkStatus.isSelected();
            habilita = true;
            break;
        }

        fieldCodigo.setEnabled(false);
        fieldDescricao.setEnabled(habilita && ativo);
        fieldUnitarioEntrada.setEnabled(habilita && ativo);
        fieldUnitarioSaida.setEnabled(habilita && ativo);
        chkStatus.setEnabled(habilita);
        fieldSiglaUnidadeMedida.setEnabled(false);
        fieldDescricaoUnidadeMedida.setEnabled(false);
        btnUnidadeMedida.setEnabled(habilita && ativo);
        fieldCodigoGrupo.setEnabled(false);
        fieldDescricaoGrupo.setEnabled(false);
        btnGrupo.setEnabled(habilita && ativo);
        fieldEstoqueAtual.setEnabled(false);
        fieldEstoqueMinimo.setEnabled(habilita && ativo);
        fieldEstoqueMaximo.setEnabled(habilita && ativo);
        fieldCodigoBarra.setEnabled(habilita && ativo);
        fieldDataUltimaCompra.setEnabled(habilita && ativo);

    }

    @Override
    public ITableModelEntidade<Produto> getTableModelEntidade() {
        return new ITableModelEntidade<Produto>() {
            @Override
            public Object getCampoEntidade(Produto entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getCodigo();
                case 1:
                    return entidade.getDescricao();
                case 2:
                    return UtilCommon.formatDecimalSomentePontoQuatroDecimais(entidade.getValorUnitarioEntrada());
                case 3:
                    dao.sincronizar(entidade);
                    return entidade.getFlagStatus().getDescricao();
                }
                return null;
            }

            @Override
            public List<Produto> getEntidades() {
                return dao.localizarTodosOrderBy();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Código", "Descrição", "Valor Unitário de Entrada", "Status" };
            }

        };
    }

    @Override
    public void validarCamposObrigatorios(Produto entidade) throws EJDLogicException {
        ValidateEntity validateEntity = new ValidateEntity();

        validateEntity.validateNotNullOrEmpty(entidade.getDescricao(), "Descrição");
        validateEntity.validateNotNullOrEmpty(entidade.getEstoqueMinimo(), "Estoque Mínimo");
        validateEntity.validateNotNullOrEmpty(entidade.getEstoqueMaximo(), "Estoque Máximo");
        validateEntity.validateNotNullOrEmpty(entidade.getUnidadeMedida().getIdUnidadeMedida(), "Unidade de Medida");
        validateEntity.validateNotNullOrEmpty(entidade.getGrupo().getIdGrupo(), "Grupo");
        validateEntity.validateNotNullOrEmpty(entidade.getValorUnitarioEntrada(), "Valor Unitário de Entrada");
        validateEntity.validateNotNullOrEmpty(entidade.getValorUnitarioSaida(), "Valor Unitário de Saída");

        validateEntity.doValidate();

    }

    @Override
    public JComponent getFirstFocusable() {
        return fieldDescricao;
    }

    @Override
    public String getTitulo() {
        return "Produto";
    }

}
