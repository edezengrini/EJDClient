package client.dialog.consultas;

import javax.inject.Inject;

import server.dao.ProdutoDAO;
import server.ejb.ProdutoEJB;
import server.entities.Produto;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import client.dialog.DialogCrud;
import client.dialog.DialogLocalizar;

public class ConsultaProduto {

    Produto produto = new Produto();
    ProdutoDAO produtoDao = new ProdutoDAO();

    @Inject
    private ProdutoEJB produtoEJB = new ProdutoEJB();

    public Produto consultarProduto(DialogCrud<Produto> dialogProduto) throws EJDLogicException {

        DialogLocalizar<Produto> formLocalizar = new DialogLocalizar<Produto>(dialogProduto, produtoDao.localizarTodos());
        formLocalizar.setVisible(true);

        Produto entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

        if (entidadeSelecionada != null) {
            produto = produtoEJB.find(entidadeSelecionada.getIdProduto());
        }

        if (produto == null) {
            throw new EJDLogicException(EnumEJDException.ENTITY_FIND, "Produto");
        }

        return produto;
    }

}
