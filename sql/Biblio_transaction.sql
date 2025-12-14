/* Transaction pour la procédure d'emprunt d'une copie de livre */
CREATE OR ALTER PROCEDURE dbo.BorrowCopies
  @username NVARCHAR(100),
  @pin CHAR(4),
  @rfids dbo.RfidList READONLY,
  @receipt_type NVARCHAR(20) = NULL  -- 'PRINT'/'EMAIL'/'NONE' (optionnel)
AS
BEGIN
  SET NOCOUNT ON;

  BEGIN TRY
    BEGIN TRAN;

    /* 1) Authentification, */
    IF NOT EXISTS (
      SELECT 1
      FROM Users
      WHERE username = @username AND user_pin = @pin
    )
    BEGIN
      THROW 50001, 'Identifiants invalides (username/pin).', 1;
    END

    /* 2) Validation: RFIDs existants */
    IF EXISTS (
      SELECT r.rfid
      FROM @rfids r
      LEFT JOIN Copies c ON c.rfid = r.rfid
      WHERE c.rfid IS NULL
    )
    BEGIN
      THROW 50002, 'Un ou plusieurs RFID n''existent pas dans le catalogue.', 1;
    END

    /* 3) Validation, empêcher d'emprunter un exemplaire déjà pris  */
    IF EXISTS (
      SELECT 1
      FROM @rfids r
      JOIN Copies c ON c.rfid = r.rfid
      WHERE c.is_borrowed = 1
    )
    BEGIN
      THROW 50003, 'Un ou plusieurs exemplaires sont déjà empruntés.', 1;
    END

    /* 4) Créer l'emprunt */
    INSERT INTO Loans (username, receipt_type)
    VALUES (@username, @receipt_type);

    DECLARE @loan_id INT = SCOPE_IDENTITY();

    /* 5) Créer les items (due_date = +14 jours) */
    INSERT INTO LoanItems (loan_id, rfid, due_date, returned_date)
    SELECT
      @loan_id,
      r.rfid,
      CAST(DATEADD(day, 14, SYSDATETIME()) AS date),
      NULL
    FROM @rfids r;

    /* 6) Marquer comme emprunté */
    UPDATE c
    SET c.is_borrowed = 1
    FROM Copies c
    JOIN @rfids r ON r.rfid = c.rfid;

    COMMIT;

    /* Résumé */
    SELECT
      @loan_id AS loan_id,
      @username AS username,
      SYSDATETIME() AS created_at,
      CAST(DATEADD(day, 14, SYSDATETIME()) AS date) AS due_date_default,
      COUNT(*) AS nb_exemplaires
    FROM @rfids;

  END TRY
  BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK;
    THROW;
  END CATCH
END
GO
