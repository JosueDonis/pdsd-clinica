package ifrn.pds.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;


//TODO Tratar algumas poss�veis exce��es!
public class Dao {
	private EntityManager entityManager;
	private Session session;
	
	private Dao() {
		System.err.println("NAAAAAAO");
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("infoclinPersistenceUnit");
		entityManager = entityManagerFactory.createEntityManager();
		conecta();
		session = new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
	}
	public static class DaoHolder{
		private static final Dao instance = new Dao();
		
		public static Dao getInstance(){
			return instance;
		}
	}
	
	public static Dao getDao(){
		return DaoHolder.getInstance();
	}
	
	/*
	 * GATO! Usando session ao inves do EntityManager. Nao esta muito elegante,  mas esta funcionando ;D
	 */
	public<E> void persist(E entity) {
		//conecta();
		//getTransaction();
		//Session session = new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		 session.persist(entity);
		 tx.commit();
		//entityManager.persist(entity);
		//session.close();
	}
	
	public<E> void update(E entity){
		//conecta();
		//Session session = new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		//E e = findById(classe, id);
		session.merge(entity);
		session.flush();
		tx.commit();
		//session.close();
	}

	private void getTransaction() {
		entityManager.getTransaction().begin();
	}

	@SuppressWarnings("unchecked")
	public<E> List<E> findAll(String classe) {
		List<E> lista;
		//conecta();
		Query q = entityManager.createQuery("select e from " + classe + " e");
		lista = q.getResultList();
		//entityManager.close();
		return lista;
	}

	//m�todo ser� usuado para fazer o controle de login
	@SuppressWarnings("unchecked")
	public<E> E findByExample(String classe, String nomeCampo, String valorCampo) {
		//conecta();
		Query q = entityManager.createQuery("SELECT e FROM " + classe + " e where " + nomeCampo + " = " + valorCampo);
		E e = (E) q.getSingleResult();
		//entityManager.close();
		return e; 
	}
	
	@SuppressWarnings("unchecked")
	public<E> E findByExampleLike(String classe, String nomeCampo, String valorCampo) {
		//conecta();
		Query q = entityManager.createQuery("SELECT e FROM " + classe + " e where " + nomeCampo + " like '%" + valorCampo + "%'");
		E e = (E) q.getSingleResult();
		//entityManager.close();
		return e; 
	}
	
	@SuppressWarnings("unchecked")
	public<E> List<E> findByExampleLikeLista(String classe, String nomeCampo, String valorCampo) {
		//conecta();
		Query q = entityManager.createQuery("SELECT e FROM " + classe + " e where " + nomeCampo + " like '%" + valorCampo + "%'");
		List<E> lista = q.getResultList();
		//entityManager.close();
		return lista; 
	}
	
	@SuppressWarnings("unchecked")
	public<E> List<E> findByExampleLista(String classe, String nomeCampo, String valorCampo) {
		//conecta();
		Query q = entityManager.createQuery("SELECT e FROM " + classe + " e where " + nomeCampo + " = " + valorCampo);
		List<E> lista = q.getResultList();
		//entityManager.close();
		return lista;
	}
	
	public<E> E findById(String classe, int id) {
		//conecta();
		Query q = entityManager.createQuery("SELECT e FROM " + classe + " e where id" + " = " + id);
		@SuppressWarnings("unchecked")
		E e = (E)q.getSingleResult();
		//entityManager.close();
		return e;
	}

	public<E> void remove(E entity) {
		//conecta();
		getTransaction();
		entityManager.remove(entity);
		commit();
		//entityManager.close();
	}
	
	public<E> boolean removeById(Class<E> classe, int id) {
		//conecta();
		getTransaction();
		Query q = entityManager.createQuery("DELETE FROM " + classe.getName()
				+ " e where e.id = " + id);
		int i = q.executeUpdate();
		commit();
		//entityManager.close();
		return i == 1;
	}

	private void commit() {
		entityManager.flush();
		entityManager.getTransaction().commit();
	}

	private void conecta() {
		Configuration cf = new AnnotationConfiguration();
		cf.configure("hibernate.cfg.xml");
	}

}
