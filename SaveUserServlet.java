
package com.venom.controller;

import com.venom.entities.Accounts;
import com.venom.entities.Logtbl;
import com.venom.entities.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;


@WebServlet(name = "SaveUserServlet", urlPatterns = {"/saveuserservlet"})
public class SaveUserServlet extends HttpServlet {
    
    @PersistenceUnit(unitName = "Venom-storePU")
    EntityManagerFactory emf;
    
    @Resource
    UserTransaction utx; 
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
       
        
        String email = request.getParameter("username");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String acctype =  request.getParameter("accountType");
        Date currentDate = Date.valueOf(LocalDate.now());
        
        AccountsJpaController ajc = new AccountsJpaController(utx, emf);
        UsersJpaController ujc = new UsersJpaController(utx, emf);
        Logtbl ltb = new Logtbl();
        //out.print(currentDate);
        try{
           utx.begin();
           EntityManager em = emf.createEntityManager();
           Users user = new Users();
           user.setEmail(email);                      
           user.setFirstName(firstname);            
           user.setLastName(lastname);           
           user.setDateCreated(currentDate);           
           em.persist(user);
           utx.commit();
           
           out.print("user added successfully");
        }
        catch(IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException ex){
            out.print("Action failed. Try again. User registered with '"+email+"' already exists");            
        }       
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
