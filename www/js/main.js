window.onload = function () {
    clearArticles();
}

/**
 * Sendet bei einem Javascript error den error an Java um ihn zu verarbeiten
 */
window.onerror = function (msg, url, line) {
    bridge.errorLog(msg, url, line);
}

/**
 * Ueberschreibt die Javascript console.log() funktion um die Nachrichten direkt an Java zu senden
 *
 * @param {String} message zu loggende Nachricht
 */
window.console.log = function (message) {
    bridge.consoleLog(message);
}

/**
 * Wird ausgeführt wenn das Dokument vollstaendig geladen wurde und das Bridge-Objekt injiziert wurde
 *
 * @param {String}  username Nutzername des derzeitigen nutzers
 * @param {Integer} usertype Nutzertyp, -1 = Visitor, 0 = Reader, 1 = Blogger
 * @param {Integer} currPage Seite auf welche die Webseite wechseln soll
 * @param {Double}  scrollPosition Position der Scrollbar
 */
function ready(username, usertype, currPage, scrollPosition) {
    this.currPage = currPage;
    this.usertype = usertype;

    document.getElementById("currUser").innerText += username;
    bridge.addPageNumbers();
    changePage(currPage);
    window.scrollTo(0, scrollPosition);
}

/**
 * Uebergibt Java die derzeitige Seite und Scrollbar position und laed die seite ueber Java neu
 */
function reloadSite() {
    bridge.reloadSite(this.currPage, window.scrollY);
}

/**
 * Versteckt Funktionen in HTML, welche nur fuer Blogger zugaenglich sein sollen.
 */
function hideBloggerFunctions() {
    document.querySelector("#create-article, #article-show").style.display = "none";
    document.querySelectorAll(".post-delete").forEach(button => {
        button.style.display = "none";
    });
}

/**
 * Versteckt Funktionen im HTML, welche nur fuer angemeldete Nutzer zugaenglich sein sollen.
 */
function hideUserFunctions() {
    hideBloggerFunctions();
    document.querySelectorAll(".post-actions").forEach(item => {
        item.style.display = "none";
    });
}

/**
 * Funktion um weitere Seiten hinzuzufügen, abhängig von der Anzahl der gesammten Artikel in der DB
 * Anzahl der Artikel / 5 = Anzahl der Seiten (ausgerunedt)
 *
 * @param {Integer} number
 */
function addPageNumbers(number) {
    var pageNav = document.querySelector("footer > nav > ul");
    for (i = 1; i <= number; i++) {
        var node = document.createElement("li");
        node.innerHTML = `<a href='#' onclick='changePage(${i});'>${i}</a>`;

        pageNav.appendChild(node)
    }
}

/**
 * Bei einem Seitenwechsel werden alle Artikel und Kommentare der aktuellen Seite wentfernt und mit den Artikel und KOmmentaren der
 * Nächsten Seite gefüllt
 *
 * @param {Integer} pagenumber
 */
function changePage(pagenumber) {
    clearArticles();
    let elements = document.querySelectorAll(`footer > nav > ul > li > a`);
    elements[this.currPage - 1].classList.remove("active");
    elements[pagenumber - 1].classList.add("active");
    this.currPage = pagenumber;
    bridge.fillWeb(pagenumber);

    if (this.usertype === 0)
        hideBloggerFunctions();
    else if (this.usertype === -1)
        hideUserFunctions();
}

/**
 * Holt sich die noetigen informationen aus dem DOM und uebergibt sie Java zum erstellen eines Artikels
 */
function postArticle() {
    let title = document.getElementById('article-title').value;
    let text = document.getElementById('article-text').value;

    bridge.createArticle(title, text);
    this.currPage = 1;
}

/**
 * Fügt einen Artikel der webview hinzu
 *
 * @param {Integer} ID Identifikator des Artikels
 * @param {String}  Title Titel des Artikels
 * @param {String}  Text Text des Artikels
 * @param {Date}    Date Datum der veroeffentlichung des Artikels
 * @param {boolean} isBlogger gibt an, ob der verfasser ein blogger ist oder nicht
 */
function displayArticle(ID, Verfasser, Title, Text, Date, isBlogger) {
    let article = document.createElement('article');
    article.id = `beitrag-${ID}`;
    article.innerHTML =
        `<h2>${Title} <span><b ${isBlogger ? "class='blogger'" : ""}>${Verfasser}</b><span class="date"> @ (${Date.replace('T', ' ')})</span></span></h2>
         <p>${Text}</p>
         <div class="post-actions">
            <button onclick="commentButtonClick(event);">Kommentieren</button> <button class="post-delete" onclick="bridge.deletePost('${article.id}', true); reloadSite();">Löschen</button>
         </div>
        <div class="comments"></div>`;

    document.getElementById("article-section").appendChild(article);
}

/**
 * TODO: FINISH JAVADOC COMMENT
 *
 * @param event
 */
function commentButtonClick(event) {
    let parent = event.currentTarget.parentElement.parentElement;
    let parentIsArticle = (parent.nodeName == 'ARTICLE');
    let commentForm = document.createElement('form');
    commentForm.classList.add('create-comment');
    commentForm.innerHTML = `<label for="${parent.id}-comment">Text:</label>
        <textarea id="${parent.id}-comment" rows="4" cols="50"></textarea>
        <button onclick="postComment('${parent.id}', ${parentIsArticle});reloadSite();">Veröffentlichen</button>`;

    event.currentTarget.parentElement.append(commentForm);
    event.currentTarget.disabled = true;
}

/**
 * Holt sich die noetigen informationen aus dem DOM und uebergibt sie Java zum erstellen eines Kommentars
 *
 * @param {String}  htmlBID Beitrag Identifikator
 * @param {boolean} parentIsArticle gibt an, ob der zu kommentierende Oberbeitrag ein Artikel oder ein Kommentar ist
 */
function postComment(htmlBID, parentIsArticle) {
    let bid = htmlBID.split('-')[1];
    let commentText = document.getElementById(`${htmlBID}-comment`).value;
    bridge.createComment(bid, commentText, parentIsArticle);
}

/**
 * Fügt in der webview einen Beitrag(Artikel/Kommentar) ein Kommentar hinzu
 *
 * @param {Integer} beitragID ID des neuen Kommentar
 * @param {Integer} kommentarID ID des Beitrags der Kommentiert werden soll
 * @param {String}  verfasser Verfasser der Kommentar
 * @param {String}  kommentarText Text des Kommentar
 */
function displayComment(kommentarID, beitragID, verfasser, kommentarText, Date, isBlogger) {
    let comment = document.createElement('div');
    comment.id = `beitrag-${kommentarID}`;
    comment.innerHTML =
        `<span class="username"><b ${isBlogger ? "class='blogger'" : ""}>${verfasser}</b><span class="date"> @ (${Date.replace('T', ' ')})</span></span>
         <p>${kommentarText}</p>
         <div class="post-actions">
            <button onclick="commentButtonClick(event);">Kommentieren</button> <button class="post-delete" onclick="bridge.deletePost('${comment.id}', false); reloadSite();">Löschen</button>
         </div>
         <div class="comments"></div>`;

    document.querySelector(`#beitrag-${beitragID} .comments`).appendChild(comment);
}

/**
 * ALle Artikel aus der webview entfernen
 */
function clearArticles() {
    document.getElementById('article-section').innerHTML = '';
}
