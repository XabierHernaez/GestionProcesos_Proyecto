package com.example.restapi.server;

import java.sql.Date;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.restapi.model.TipoPago;
import com.example.restapi.model.Usuario;
import com.example.restapi.server.jdo.UsuarioJDO;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/resource")
@Produces("application/json")
public class Resource {
    protected static final Logger logger = LogManager.getLogger();

    private int cont = 0;
    private PersistenceManager pm=null;
	private Transaction tx=null;
	/**Constructor que inicializa el servidor. */
    public Resource(){
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
    }
	/**Metodo que registra un cliente en la base de datos. */
    @POST
	@Path("/register")
	public Response registrarCliente(Usuario usuario) {
		try {	
            tx.begin();
            logger.info("Comprobando que el usuario no exista: '{}'", usuario.getEmail());
			UsuarioJDO usuarioJDO = null;
			try {
				usuarioJDO = pm.getObjectById(UsuarioJDO.class, usuario.getEmail());
			} catch (JDOObjectNotFoundException ex1) {
				logger.error("Exception launched: {}", ex1.getMessage());
			}
			logger.info("Cliente: {}", usuarioJDO);
			if (usuarioJDO != null) {
				logger.info("Añadiendo contraseña: {}", usuarioJDO);
				usuarioJDO.setPassword(usuario.getPassword());
				logger.info("Contraseña añadida: {}", usuarioJDO);
                logger.info("Añadiendo nombre: {}", usuarioJDO);
				usuarioJDO.setNombre(usuario.getNombre());
				logger.info("Nombre Añadido: {}", usuarioJDO);
                logger.info("Añadiendo apellidos: {}", usuarioJDO);
                usuarioJDO.setApellidos(usuario.getApellidos());
                logger.info("Apellidos añadidos: {}", usuarioJDO);
                logger.info("Añadiendo telefono: {}", usuarioJDO);
                usuarioJDO.setTelefono(usuario.getTelefono());
                logger.info("Telefono añadido: {}", usuarioJDO);
                logger.info("Añadiendo fecha de nacimiento: {}", usuarioJDO);
                usuarioJDO.setFechaNacimiento(usuario.getFechaNacimiento());
                logger.info("Fecha de nacimiento añadida: {}", usuarioJDO);
                logger.info("Añadiendo tipo de usuario: {}", usuarioJDO);
                usuarioJDO.setTipoUsuario(usuario.getTipoUsuario());
                logger.info("Tipo de usuario añadido: {}", usuarioJDO);
                logger.info("Añadiendo tipo de pago: {}", usuarioJDO);
                usuarioJDO.setTipoPago(usuario.getTipoPago());
                logger.info("Tipo de pago añadido: {}", usuarioJDO);
                logger.info("Usuario actualizado: {}", usuarioJDO);
                


			} else {
				logger.info("Creando usuario: {}", usuarioJDO);
				usuarioJDO = new UsuarioJDO( usuario.getDni(),usuario.getNombre(), usuario.getApellidos()
                ,usuario.getEmail(), usuario.getPassword(), usuario.getTelefono(), usuario.getFechaNacimiento(),
                 usuario.getTipoPago(), usuario.getTipoUsuario());

				pm.makePersistent(usuarioJDO);
				logger.info("Usuario creado: {}", usuarioJDO);
			}
			tx.commit();
			return Response.ok().build();
        }
        finally {
            if (tx.isActive())
            {
                tx.rollback();
            }
			//pm.close();
		}
	}
}