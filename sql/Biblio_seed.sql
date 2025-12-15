/* Seed pour l'insertion dans la base de données */
USE BIBLIO;

GO

INSERT INTO Users (username, user_pin) VALUES
('Jean123', '1234'),
('Jeanne345', '5678');
GO

INSERT INTO Books (title, author, edition, release_year, page_number) VALUES
(N'Clean Code', N'Robert C. Martin', N'1st', 2008, 464),
(N'The Pragmatic Programmer', N'Andrew Hunt; David Thomas', N'2nd', 2019, 352),
(N'Design Patterns', N'Erich Gamma et al.', N'1st', 1994, 395),
(N'Refactoring', N'Martin Fowler', N'2nd', 2018, 448),
(N'Introduction to Algorithms', N'Cormen et al.', N'3rd', 2009, 1312),
(N'Effective Java', N'Joshua Bloch', N'3rd', 2018, 416),
(N'Operating System Concepts', N'Silberschatz et al.', N'10th', 2018, 976),
(N'Computer Networks', N'Andrew S. Tanenbaum', N'5th', 2011, 951),
(N'Artificial Intelligence: A Modern Approach', N'Russell; Norvig', N'4th', 2020, 1152),
(N'Head First Design Patterns', N'Eric Freeman; Elisabeth Robson', N'2nd', 2020, 672);
GO

;WITH b AS (
  SELECT book_id, ROW_NUMBER() OVER (ORDER BY book_id) AS rn
  FROM Books
)
INSERT INTO Copies (rfid, book_id, is_borrowed)
SELECT
  RIGHT('000000' + CAST(100000 + (rn-1)*2 AS varchar(6)), 6) AS rfid,
  book_id,
  0
FROM b
UNION ALL
SELECT
  RIGHT('000000' + CAST(100000 + (rn-1)*2 + 1 AS varchar(6)), 6) AS rfid,
  book_id,
  0
FROM b;
GO