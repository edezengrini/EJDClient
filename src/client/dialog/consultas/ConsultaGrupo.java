package client.dialog.consultas;

import javax.inject.Inject;

import server.dao.GrupoDAO;
import server.ejb.GrupoEJB;
import server.entities.Grupo;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import client.dialog.DialogCrud;
import client.dialog.DialogLocalizar;

public class ConsultaGrupo {

    Grupo grupo = new Grupo();
    GrupoDAO grupoDao = new GrupoDAO();

    @Inject
    private GrupoEJB grupoEJB = new GrupoEJB();

    public Grupo consultarGrupos(DialogCrud<Grupo> dialogGrupo) throws EJDLogicException {

        DialogLocalizar<Grupo> formLocalizar = new DialogLocalizar<Grupo>(dialogGrupo, grupoDao.localizarTodos());
        formLocalizar.setVisible(true);

        Grupo entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

        if (entidadeSelecionada != null) {
            grupo = grupoEJB.find(entidadeSelecionada.getIdGrupo());
        }

        if (grupo == null) {
            throw new EJDLogicException(EnumEJDException.ENTITY_FIND, "Grupo");
        }

        return grupo;
    }

}
