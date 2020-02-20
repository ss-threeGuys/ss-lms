package com.smoothstack.borrower.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smoothstack.borrower.daos.BorrowerDAO;
import com.smoothstack.borrower.domain.CheckOutDetails;
import com.smoothstack.borrower.exceptions.InvalidCardNumberException;

@Service
@Transactional
public class BorrowerServices implements IBorrowerServices {

	private static final Logger log = LoggerFactory.getLogger(BorrowerServices.class);

	@Autowired
	private BorrowerDAO bdao;

	@Override
	public CheckOutDetails checkOutBook(Integer branchId, Integer bookId, Integer cardNo)
			throws SQLException, ClassNotFoundException, InvalidCardNumberException {
		CheckOutDetails details = null;
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, +7);
			Date dueDate = cal.getTime();
			boolean status = bdao.checkOut(branchId, bookId, cardNo);
			
		//	conn.commit();
			
			String bookTitle = bdao.getBookTitle(bookId);
			details = new CheckOutDetails(bookTitle, dueDate.toString());
			
			
		} catch (ClassNotFoundException | SQLException | InvalidCardNumberException  e) {
			if(conn != null) {
		//	conn.rollback();
			}
			log.error("Please try again, as there was a database error. Unable to make changes.");
			throw e;
		}
		return details;
	}

	@Override
	public boolean checkInBook(Integer bookId, Integer cardNo) throws ClassNotFoundException, SQLException, InvalidCardNumberException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			boolean status = bdao.returnBook(bookId, cardNo);
		//	conn.commit();
			return status;

		} catch (ClassNotFoundException | SQLException | InvalidCardNumberException  e) {
			if(conn != null) {
		//	conn.rollback();
			}
		
			log.error("Please try again, as there was a database error. Unable to make changes.");
			throw e;
		}
		
	}

}
