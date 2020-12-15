window.onload = function() {
    clearArticles();
}

window.onerror = function (msg, url, line) {
    bridge.errorLog(msg, url, line);
}

window.console.log = function (message) {
    bridge.consoleLog(message);
}

function ready(username, usertype, currPage, scrollPosition) {
    this.currPage = currPage;
    this.usertype = usertype;

    document.getElementById("currUser").innerText += username + (usertype === 0 ? " Visitor" : (usertype === 1 ? " (Reader)" : " (Blogger)"));
    bridge.addPage();
    changePage(currPage);
    window.scrollTo(0, scrollPosition);
}

function reloadSite() {
    bridge.reloadSite(this.currPage, window.scrollY);
}

function hideBloggerFunctions() {
    document.querySelector("#create-article, #article-show").style.display = "none";
    document.querySelectorAll(".post-delete").forEach(button => {
       button.style.display = "none";
    });
}

function hideUserFunctions() {
    hideBloggerFunctions();
    document.querySelectorAll(".post-actions").forEach(item => {
        item.style.display = "none";
    });
}

// Funktion um weitere Seiten hinzuzufügen
function Page(number){
    var pageNav = document.querySelector("footer > nav > ul");
    for (i = 1; i <=number; i++ ) {
        var node = document.createElement("li");
        node.innerHTML = `<a href='#' onclick='changePage(${i});'>${i}</a>`;

        pageNav.appendChild(node) //TODO: Anpassen sobald das Layout fertig. Als Beispiel <div class="message">Add Message<br>Title: <input type="text usw.
    }
}

function changePage(pagenumber) {
    this.currPage = pagenumber;
    clearArticles();
    bridge.fillWeb(pagenumber);

    var elements = document.querySelectorAll(`footer > nav > ul > li > a`);
    for (let i = 0; i < elements.length; i++) {
        if(elements[i].innerHTML == pagenumber) {
            elements[i].classList.add("active");
        } else {
            elements[i].classList.remove("active");
        }
    }

    // Regeln Anwenden
    if (this.usertype === 1)
        hideBloggerFunctions();
    else if (this.usertype === 0)
        hideUserFunctions();
}

function createArticle() {
    let title = document.getElementById('article-title').value;
    let text = document.getElementById('article-text').value;

    bridge.createArticle(title, text);
    this.currPage = 1;
}

/**
 * Fügt einen Artikel der webview hinzu
 * @param ID
 * @param Title
 * @param Text
 */
function displayArticle(ID, Verfasser, Title, Text) {
    let article = document.createElement('article');
    article.id = `beitrag-${ID}`;
    article.innerHTML =
        `<h2>${Title}</h2>
         <p>${Text}</p>
         <div class="post-actions">
            <button onclick="commentButtonClick(event);">Kommentieren</button> <button class="post-delete" onclick="bridge.deleteBeitrag('${article.id}', true); reloadSite();">Löschen</button>
         </div>
        <div class="comments"></div>`;

    document.getElementById("article-section").appendChild(article);
}

function commentButtonClick(event) {
    let bid = event.currentTarget.parentElement.parentElement.id;

    let commentForm = document.createElement('form');
    commentForm.classList.add('create-comment');
    commentForm.innerHTML = `<label for="${bid}-comment">Text:</label>
        <textarea id="${bid}-comment" rows="4" cols="50"></textarea>
        <button onclick="postComment('${bid}');reloadSite();">Veröffentlichen</button>`;

    event.currentTarget.parentElement.append(commentForm);
    event.currentTarget.disabled = true;
}

function postComment(htmlBID) {
    let bid = htmlBID.split('-')[1];
    let commentText = document.getElementById(`${htmlBID}-comment`).value;
    bridge.createComment(bid, commentText);
}

/**
 * Fügt in der webview einen Beitrag(Artikel/Kommentar) ein Kommentar hinzu
 * @param beitragID ID des neuen Kommentar
 * @param kommentarID ID des Beitrags der Kommentiert werden soll
 * @param verfasser Verfasser der Kommentar
 * @param kommentarText Text des Kommentar
 */
function displayComment(kommentarID, beitragID, verfasser, kommentarText) {
    let comment = document.createElement('div');
    comment.id = `beitrag-${kommentarID}`;
    comment.innerHTML = `<span class="username">${verfasser}</span>
                    <p>${kommentarText}</p>
                    <div class="post-actions">
                        <button onclick="commentButtonClick(event);">Kommentieren</button> <button class="post-delete" onclick="bridge.deleteBeitrag('${comment.id}', false); reloadSite();">Löschen</button>
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


