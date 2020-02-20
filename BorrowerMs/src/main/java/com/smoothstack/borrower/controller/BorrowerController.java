package com.smoothstack.borrower.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.smoothstack.borrower.domain.CheckOutDetails;
import com.smoothstack.borrower.exceptions.InvalidCardNumberException;
import com.smoothstack.borrower.services.BorrowerServices;
import com.smoothstack.borrower.util.HeaderUtils;

@RestController

@RequestMapping("/loans")
public class BorrowerController {

	@Autowired
	private BorrowerServices borrowerService = new BorrowerServices();
	private static final Logger log = LoggerFactory.getLogger(BorrowerController.class);

	@RequestMapping(value = "/new/{branchId}/{bookId}/{cardNo}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CheckOutDetails> checkOutDetails(@PathVariable("branchId") Integer branchId,
			@PathVariable("bookId") Integer bookId, @PathVariable("cardNo") Integer cardNo){

		try {

			CheckOutDetails details = borrowerService.checkOutBook(branchId, bookId, cardNo);
			return new ResponseEntity<>(details, null, HttpStatus.OK);

		} catch (ClassNotFoundException | SQLException | InvalidCardNumberException e) {

			log.error("Please try again, as there was a database error. Unable to make changes." + e.getMessage());

			return ResponseEntity.badRequest()
					.headers(HeaderUtils.createFailureAlert("Check out details", "Failed to check out", e.getMessage()))
					.body(null);

		}

	}

	@RequestMapping(value = "/return/{bookId}/{cardNo}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkInBook(@PathVariable("bookId") Integer bookId,
			@PathVariable("cardNo") Integer cardNo) {

		String resp = "";
		log.debug("bookId = " + bookId);
		try {
			boolean status = borrowerService.checkInBook(bookId, cardNo);
			if (status) {
				resp = "Book has been checked in successfully.";

				return new ResponseEntity<String>(resp, null, HttpStatus.OK);
			} else {
				resp = "Failed to check in, please try again.";
				return new ResponseEntity<String>(resp, null, HttpStatus.NOT_MODIFIED);

			}

		} catch (ClassNotFoundException | SQLException | InvalidCardNumberException e) {

			log.error("Please try again, as there was a database error. Unable to make changes." + e.getMessage());
			return new ResponseEntity<>("Check In book failed" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}
}
