package client.dialog.consultas;

import javax.inject.Inject;

import server.dao.UnidadeMedidaDAO;
import server.ejb.UnidadeMedidaEJB;
import server.entities.UnidadeMedida;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import client.dialog.DialogCrud;
import client.dialog.DialogLocalizar;

public class ConsultaUnidadeMedida {

    UnidadeMedida unidadeMedida = new UnidadeMedida();
    UnidadeMedidaDAO unidadeMedidaDao = new UnidadeMedidaDAO();

    @Inject
    private UnidadeMedidaEJB unidadeMedidaEJB = new UnidadeMedidaEJB();

    public UnidadeMedida consultarUnidadesMedidas(DialogCrud<UnidadeMedida> dialogUnidadeMedida) throws EJDLogicException {

        DialogLocalizar<UnidadeMedida> formLocalizar = new DialogLocalizar<UnidadeMedida>(dialogUnidadeMedida, unidadeMedidaDao.localizarTodos());
        formLocalizar.setVisible(true);

        UnidadeMedida entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

        if (entidadeSelecionada != null) {
            unidadeMedida = unidadeMedidaEJB.find(entidadeSelecionada.getIdUnidadeMedida());
        }

        if (unidadeMedida == null) {
            throw new EJDLogicException(EnumEJDException.ENTITY_FIND, "Unidade de Medida");
        }

        return unidadeMedida;
    }

    public UnidadeMedida consultarUnidadeMedida(String sigla) {
        return unidadeMedidaEJB.findBySigla(sigla);
    }

}
