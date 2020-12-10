var usertype = 0;

window.onload = function() {
    clearArticles();
}

window.onerror = function (msg, url, line) {
    bridge.errorLog(msg, url, line);
}

function ready(username, isblogger) {
    document.getElementById("currUser").innerText += username + (username !== ` ` ? (isblogger ? " (Blogger)" : " (Reader)") : " Visitor");
    addP();
}

function setUsertype(usertype) {
    this.usertype = usertype;
}

function hideBloggerFunctions() {
    //return; //FIXME: debug
    document.querySelector("#create-article").style.display = "none";
    document.querySelectorAll(".post-delete").forEach(button => {
       button.style.display = "none";
    });
}

function hideUserFunctions() {
    //return; //FIXME: debug
    hideBloggerFunctions();
    document.querySelectorAll(".post-actions").forEach(item => {
        item.style.display = "none";
    });
}

//Hilfsfunktion um die Seitenanzahl aus Java per Bride zu verwenden
function addP() {
    bridge.addPage();
    bridge.fillWeb(1);
}

// Funktion um weitere Seiten hinzuzufügen
function Page(number){
    var pageNav = document.querySelector("footer > nav > ul");
    for (i = 1; i <=number; i++ ) {
        var node = document.createElement("li");
        node.innerHTML = `<a href='#' onclick='changePage(${i});'>${i}</a>`;

        //Default page
        if(i == 1) {
            node.firstChild.classList.add("active");
        }

        pageNav.appendChild(node) //TODO: Anpassen sobald das Layout fertig. Als Beispiel <div class="message">Add Message<br>Title: <input type="text usw.
    }
}

function changePage(pagenumber) {
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
    bridge.consoleLog(this.usertype);

    if (this.usertype === 1)
        hideBloggerFunctions();
    else if (this.usertype === 0)
        hideUserFunctions();
    //TODO: nach jedem seitenwechsel auch regeln anwenden
}

function createArticle() {
    let title = document.getElementById('article-title').value;
    let text = document.getElementById('article-text').value;

    bridge.createArticle(title, text);
    bridge.reloadSite();
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
            <button onclick="commentButtonClick(event);">Kommentieren</button> <button class="post-delete" onclick="deleteArticle('${article.id}');">Löschen</button>
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
        <button type="submit" onclick="postComment('${bid}');">Veröffentlichen</button>`;

    event.currentTarget.parentElement.append(commentForm);
    event.currentTarget.disabled = true;
}

function postComment(htmlBID) {
    let bid = htmlBID.split('-')[1];
    let commentText = document.getElementById(`${htmlBID}-comment`).value;
    bridge.createComment(bid, commentText);

    bridge.reloadSite();
    //bridge.consoleLog(`htmlBID: ${htmlBID}\nComment: ${commentText}`); //FIXME: debug only
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
                        <button onclick="commentButtonClick(event);">Kommentieren</button> <button class="post-delete" onclick="deleteComment('${comment.id}');">Löschen</button>
                    </div>
                    <div class="comments"></div>`;

    document.querySelector(`#beitrag-${beitragID} .comments`).appendChild(comment);
}

/**
 * Beitrag aus der webview löschen
 * @param id
 */
function deleteArticle(id) {
    var items = document.querySelectorAll(`#${id} div`);
    var itemsarr = Array.from(items).filter(item => item.id !== "").reverse();
    itemsarr.forEach( item => {
        bridge.deleteComment(item.id);
    });
    bridge.deleteArticle(id);

    //document.getElementById(id).remove();
    bridge.reloadSite();
}

function deleteComment(id) {
    var items = document.querySelectorAll(`#${id} div`);
    var itemsarr = Array.from(items).filter(item => item.id !== "").reverse();
    itemsarr.forEach( item => {
        bridge.deleteComment(item.id);
    });
    bridge.deleteComment(id);

    //document.getElementById(id).remove();
    bridge.reloadSite();
}

/**
 * Artikel aus der webview löschen
 */
function clearArticles() {
    document.getElementById('article-section').innerHTML = '';
}
