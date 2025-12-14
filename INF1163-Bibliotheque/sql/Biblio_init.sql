/*Initialisation dans la base de données */
USE BIBLIO;

CREATE TABLE Users (
  username NVARCHAR(100) NOT NULL PRIMARY KEY,       
  user_pin CHAR(4) NOT NULL,
  CONSTRAINT CK_user_pin_4digits CHECK (user_pin LIKE '[0-9][0-9][0-9][0-9]')
);

CREATE TABLE Books (
  book_id INT IDENTITY PRIMARY KEY,
  title NVARCHAR(255) NOT NULL,
  author NVARCHAR(100) NOT NULL,
  edition NVARCHAR(100) NOT NULL,
  release_year INT NOT NULL,
  page_number INT NOT NULL
);

CREATE TABLE Copies (
  rfid CHAR(6) NOT NULL PRIMARY KEY,
  book_id INT NOT NULL,
  is_borrowed BIT NOT NULL DEFAULT 0,
  CONSTRAINT CK_rfid_6digits CHECK (rfid LIKE '[0-9][0-9][0-9][0-9][0-9][0-9]'),
  CONSTRAINT FK_Copies_Books FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

CREATE TABLE Loans (
  loan_id INT IDENTITY PRIMARY KEY,
  username NVARCHAR(100) NOT NULL,
  loan_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
  receipt_type NVARCHAR(20) NULL, 
  CONSTRAINT FK_Loans_Users FOREIGN KEY (username) REFERENCES Users(username)
);

CREATE TABLE LoanItems (
  loan_id INT NOT NULL,
  rfid CHAR(6) NOT NULL,
  due_date DATE NOT NULL,
  returned_date DATE NULL,
  PRIMARY KEY (loan_id, rfid),
  CONSTRAINT FK_LoanItems_Loans FOREIGN KEY (loan_id) REFERENCES Loans(loan_id),
  CONSTRAINT FK_LoanItems_Copies FOREIGN KEY (rfid) REFERENCES Copies(rfid)
);
