CREATE DATABASE Artconnect;

USE Artconnect;

CREATE TABLE Artist( 
artist_id INT, 
email VARCHAR(50), 
birthyear INT, 
city VARCHAR(50), 
PRIMARY KEY(artist_id) 
);
CREATE TABLE Artwork( 
artwork_id INT, 
Title VARCHAR(50), 
Type VARCHAR(50), 
Status VARCHAR(50), 
Price INT, 
artist_id INT NOT NULL, 
PRIMARY KEY(artwork_id), 
FOREIGN KEY(artist_id) REFERENCES Artist(artist_id) 
); 
CREATE TABLE Gallery( 
Gallery_id INT, 
Location VARCHAR(50), 
Name VARCHAR(50), 
PRIMARY KEY(Gallery_id) 
); 
CREATE TABLE Workshop( 
workshop_id INT, 
Title VARCHAR(50), 
date_ DATETIME, 
price DECIMAL(15,2), 
level VARCHAR(50), 
artist_id INT NOT NULL, 
PRIMARY KEY(workshop_id), 
FOREIGN KEY(artist_id) REFERENCES Artist(artist_id) 
); 
CREATE TABLE Member ( 
user_id INT, 
Name_member VARCHAR(50), 
Email VARCHAR(50), 
city VARCHAR(50), 
PRIMARY KEY(user_id) 
); 

CREATE TABLE Review( 
review_id INT, 
Date_ DATE, 
rating DECIMAL(15,2), 
comment VARCHAR(50), 
artwork_id INT NOT NULL, 
user_id INT NOT NULL, 
PRIMARY KEY(review_id), 
FOREIGN KEY(artwork_id) REFERENCES Artwork(artwork_id), 
FOREIGN KEY(user_id) REFERENCES Member(user_id) 
);

CREATE TABLE Exhibition( 
exhibition_id INT, 
Theme VARCHAR(50), 
Entitled VARCHAR(50), 
Start_date DATE, 
Gallery_id INT NOT NULL, 
PRIMARY KEY(exhibition_id), 
FOREIGN KEY(Gallery_id) REFERENCES Gallery(Gallery_id) 
); 
CREATE TABLE Participate( 
workshop_id INT, 
user_id INT, 
PRIMARY KEY(workshop_id, user_id), 
FOREIGN KEY(workshop_id) REFERENCES Workshop(workshop_id), 
FOREIGN KEY(user_id) REFERENCES Member(user_id) 
); 

CREATE TABLE Expose( 
exhibition_id INT, 
artwork_id INT, 
PRIMARY KEY(exhibition_id, artwork_id), 
FOREIGN KEY(exhibition_id) REFERENCES Exhibition(exhibition_id), 
FOREIGN KEY(artwork_id) REFERENCES Artwork(artwork_id) 
); 

-- Prompt CHATGPT : Peux-tu me faire des insert dans cette BDD stp, respecte la structure de ces tables: [insertion du code précédent]
-- ARTIST
INSERT INTO Artist VALUES
(1, 'monet@gmail.com', 1840, 'Paris'),
(2, 'vangogh@gmail.com', 1853, 'Zundert'),
(3, 'picasso@gmail.com', 1881, 'Malaga');

-- ARTWORK
INSERT INTO Artwork VALUES
(1, 'Water Lilies', 'Painting', 'Available', 500000, 1),
(2, 'Starry Night', 'Painting', 'Sold', 1000000, 2),
(3, 'Guernica', 'Painting', 'Exhibited', 2000000, 3);

-- GALLERY
INSERT INTO Gallery VALUES
(1, 'Paris', 'Louvre'),
(2, 'Amsterdam', 'Van Gogh Museum'),
(3, 'Madrid', 'Reina Sofia');

INSERT into Gallery VALUES
(4, 'Paris', 'Musée du Petit Palais');

-- WORKSHOP
INSERT INTO Workshop VALUES
(1, 'Impressionism Basics', '2026-05-10 10:00:00', 150.00, 'Beginner', 1),
(2, 'Post-Impressionism', '2026-06-15 14:00:00', 200.00, 'Intermediate', 2),
(3, 'Cubism Advanced', '2026-07-20 09:00:00', 250.00, 'Advanced', 3);

-- MEMBER
INSERT INTO Member VALUES
(1, 'Alice Dupont', 'alice@gmail.com', 'Paris'),
(2, 'Bob Martin', 'bob@gmail.com', 'Lyon'),
(3, 'Charlie Durand', 'charlie@gmail.com', 'Marseille');

-- REVIEW
INSERT INTO Review VALUES
(1, '2026-03-01', 4.5, 'Magnifique', 1, 1),
(2, '2026-03-05', 5.0, 'Incroyable', 2, 2),
(3, '2026-03-10', 4.0, 'Très bien', 3, 3);

-- EXHIBITION
INSERT INTO Exhibition VALUES
(1, 'Impressionism', 'Light & Nature', '2026-04-01', 1),
(2, 'Post-Impressionism', 'Colors of Emotion', '2026-04-15', 2),
(3, 'Cubism', 'Shapes & Forms', '2026-05-01', 3);

INSERT INTO Exhibition VALUES
(4, 'Realism', 'True Nature', '2027-01-03', 3);

-- PARTICIPATE
INSERT INTO Participate VALUES
(1, 1),
(2, 2),
(3, 3);

-- EXPOSE
INSERT INTO Expose VALUES
(1, 1),
(2, 2),
(3, 3);

-- On peut créer un catalogue des expositions
CREATE VIEW  catalogue AS 
SELECT exhibition_id, Theme, Entitled, Start_date, Location, Name
FROM Exhibition JOIN Gallery ON Exhibition.Gallery_id = Gallery.Gallery_id;

SELECT * FROM catalogue;

-- On peut créer une vue d'une oeuvre avec toutes les infos sur les avis
CREATE VIEW oeuvre_notes AS
SELECT Title, date_, rating, comment FROM Artwork INNER JOIN Review
ON Artwork.artwork_id = Review.artwork_id;

SELECT * from oeuvre_notes;

-- 
CREATE VIEW v_exhibition_catalog AS  
SELECT 
e.exhibition_id, 
e.Entitled AS exposition, 
e.Theme, 
e.Start_date, 
g.name AS galerie, 
g.Location, 
a.artwork_id, 
a.Title AS oeuvre, 
a.Type, 
ar.artist_id
FROM Exhibition e 
JOIN Gallery g ON e.Gallery_id = g.Gallery_id 
JOIN Expose ex ON e.exhibition_id = ex.exhibition_id 
JOIN Artwork a ON ex.artwork_id = a.artwork_id 
JOIN Artist ar ON a.artist_id = ar.artist_id; 

SELECT * FROM v_exhibition_catalog;
-- -----------
-- Vue permettant d'afficher les informations publiques de tous les profils
CREATE VIEW v_member_public AS 
SELECT 
user_id, 
Name_member, 
city 
FROM Member; 

SELECT * FROM v_member_public;


-- INDEX 1 – Recherche fréquente : œuvres par artiste 
-- Justification : la jointure Artwork et Artist est omniprésente ; 
-- sans index, chaque requête scanne toute la table Artwork. 
CREATE INDEX idx_artwork_artist ON Artwork(artist_id); 

-- INDEX 2 – Filtrage par statut d'œuvre 
-- Justification : les requêtes "WHERE Status = 'Disponible'" sont 
-- très courantes sur la page catalogue publique. 
CREATE INDEX idx_artwork_status ON Artwork(Status); 

-- INDEX 3 – Recherche d'ateliers par date 
-- Justification : l'affichage "prochains ateliers" trie/filtre sur 
-- date_. Un index sur cette colonne accélère drastiquement la requête. 
CREATE INDEX idx_workshop_date ON Workshop(date_); 

DELIMITER $$ 
-- TRIGGER 1 – La note d'un avis doit être entre 0 et 5 
CREATE TRIGGER trg_review_rating 
BEFORE INSERT ON Review 
FOR EACH ROW 
BEGIN 
IF NEW.rating < 0 OR NEW.rating > 5 THEN 
SIGNAL SQLSTATE '45000' 
SET MESSAGE_TEXT = 'La note doit être entre 0 et 5.'; 
END IF; 
END$$ 


-- TRIGGER 2 – Le prix d'une œuvre doit être positif 
-- Trigger contrôlant le prix d'une oeuvre
CREATE TRIGGER trg_artwork_price 
BEFORE INSERT ON Artwork 
FOR EACH ROW 
BEGIN 
IF NEW.Price < 0 THEN 
SIGNAL SQLSTATE '45000' 
SET MESSAGE_TEXT = 'Le prix d\'une œuvre ne peut pas être négatif.'; 
END IF; 
END$$ 
 
 -- TRIGGER 3 – Le prix d'un atelier doit être positif 
CREATE TRIGGER trg_workshop_price 
BEFORE INSERT ON Workshop 
FOR EACH ROW 
BEGIN 
IF NEW.price < 0 THEN 
SIGNAL SQLSTATE '45000' 
SET MESSAGE_TEXT = 'Le prix d\'un atelier ne peut pas être négatif.'; 
END IF; 
END$$ 
  
DELIMITER ; 
  
DELIMITER $$ 
 -- Procedure analysant si l'inscription est complete 
CREATE PROCEDURE sp_inscription_membre_complete( 
    IN p_user_id        INT, 
    IN p_name           VARCHAR(50), 
    IN p_email          VARCHAR(50), 
    IN p_city           VARCHAR(50), 
    IN p_workshop_id    INT, 
    IN p_artwork_id     INT, 
    IN p_rating         DECIMAL(15,2), 
    IN p_comment        VARCHAR(50) 
) 
BEGIN 
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN 
        ROLLBACK; 
        SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Erreur : transaction annulée, aucune modification effectuée.'; 
    END; 
 
DELIMITER // 
 
 -- Procedure analysant si l'artiste d'une oeuvre est bien répertoriré
CREATE PROCEDURE AjouterArtwork( 
    IN p_id INT, 
    IN p_titre VARCHAR(50), 
    IN p_type VARCHAR(50), 
    IN p_status VARCHAR(50), 
    IN p_prix INT, 
    IN p_artist_id INT 
) 
BEGIN 
    IF EXISTS (SELECT 1 FROM Artist WHERE artist_id = p_artist_id) THEN 
        INSERT INTO Artwork (artwork_id, Title, Type, Status, Price, artist_id) 
        VALUES (p_id, p_titre, p_type, p_status, p_prix, p_artist_id); 
        SELECT 'L''œuvre a été ajoutée avec succès.' AS Message; 
    ELSE 
        SELECT 'Erreur : Cet artiste n''existe pas.' AS Message; 
    END IF; 
END // 
 
DELIMITER ; 
 
DELIMITER // 

-- Procedure qui permet d'obtenir le prix moyen de vente d'un artiste 
CREATE PROCEDURE GetStatistiquesArtiste( 
    IN p_artist_id INT 
) 
BEGIN 
    SELECT  
        A.artist_id, 
        A.email, 
        COUNT(W.artwork_id) AS Nombre_Oeuvres, 
        AVG(W.Price) AS Prix_Moyen 
    FROM Artist A 
    LEFT JOIN Artwork W ON A.artist_id = W.artist_id 
    WHERE A.artist_id = p_artist_id 
    GROUP BY A.artist_id; 
END // 
 
DELIMITER ;

START TRANSACTION; 
  
    INSERT INTO Member(user_id, Name_member, Email, city) 
    VALUES (p_user_id, p_name, p_email, p_city); 
  
    -- ÉTAPE 2 : Inscrire le membre à l'atelier 
    -- (la FK garantit que le workshop_id existe, sinon erreur : rollback) 
    INSERT INTO Participate(workshop_id, user_id) 
    VALUES (p_workshop_id, p_user_id); 
  
     -- (le trigger trg_review_rating vérifie que la note est entre 0 et 5) 
    INSERT INTO Review(review_id, Date_, rating, comment, artwork_id, user_id) 
    VALUES ( 
        (SELECT IFNULL(MAX(review_id), 0) + 1 FROM Review r), 
        CURDATE(), 
        p_rating, 
        p_comment, 
        p_artwork_id, 
        p_user_id 
    ); 
  
    COMMIT; 
END$$ 
  
DELIMITER ; 

-- Ajout de la colonne "name" pour l'artiste
ALTER TABLE artist ADD COLUMN name VARCHAR(100) NOT NULL DEFAULT '' AFTER artist_id;

-- Modification du statut pour plus de clarté
SELECT DISTINCT Status FROM artwork;
UPDATE artwork SET Status = 'FOR_SALE' WHERE Status = 'Available';
-- 'Sold' et 'Exhibited' sont proches mais la casse diffère
UPDATE artwork SET Status = 'SOLD' WHERE Status = 'Sold';
UPDATE artwork SET Status = 'EXHIBITED' WHERE Status = 'Exhibited';
ALTER TABLE artwork MODIFY Status ENUM('FOR_SALE', 'SOLD', 'EXHIBITED');


-- Ajout d'une colonne creationYear dans la table Artwork 
ALTER TABLE artwork ADD COLUMN creationYear VARCHAR(100) NOT NULL DEFAULT '' AFTER artist_id;

-- dernières modifications du 17/05/2026
DELETE FROM member WHERE user_id = 4; -- suppression doublon
INSERT INTO Member VALUES (4, 'Jean Paul', 'jp@gmail.com', 'Thiais');
INSERT INTO Review VALUES (4, '2026-05-10', 3.5, 'Interessant, je reviendrai !', 3, 2); -- fonctionnel


-- INSERT INTO Artist VALUES(4, 'Anastasia Kreslina', 'NK@gmail.ru', 1990, 'Riga'); -- fonctionnel