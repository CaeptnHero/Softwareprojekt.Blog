window.onload = function() {
    clearArticles();
}

window.onerror = function (msg, url, line) {
    bridge.errorLog(msg, url, line);
}

function ready() {
    addP();
}

function hideBloggerFunctions() {
    document.querySelector("#create-article").style.display = "none";
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

function fill(art, s) {
    // document.getElementById("titel"+ s).innerHTML = art["titel"];
    // document.getElementById("text" + s).innerHTML = art["text"];

    //FIXME: tmp fix
    displayArticle(-1, art.titel, art.text);
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
}

function createArticle() {
    let title = document.getElementById('article-title').value;
    let text = document.getElementById('article-text').value;

    bridge.createArticle(title, text);
}

/**
 * Fügt einen Artikel der webview hinzu
 * @param ID
 * @param Title
 * @param Text
 */
function displayArticle(ID, Title, Text) {
    let article = document.createElement('article');
    article.id = `beitrag-${ID}`;
    article.innerHTML =
        `<h2>${Title}</h2>
         <p>${Text}</p>
         <div class="post-actions">
            <button onclick="createComment();">Kommentieren</button> <button class="post-delete" onclick="deletePost('${article.id}');">Löschen</button>
         </div>
        <div class="comments"></div>`;

    document.getElementById("article-section").appendChild(article);
}

function createComment() {
    bridge.consoleLog("Create Comment debug");
    //bridge.createComment(1, 1, "", ""); //TODO: implement
    displayComment(-1, -1, "", "");
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
                        <button>Kommentieren</button> <button class="post-delete" onclick="deletePost('${comment.id}');">Löschen</button>
                    </div>
                    <div class="comments"></div>`;

    document.querySelector(`#beitrag-${beitragID} .comments`).appendChild(comment);
}

/**
 * Beitrag aus der webview löschen
 * @param id
 */
function deletePost(id) {
    document.getElementById(id).remove();
}

/**
 * Artikel aus der webview löschen
 */
function clearArticles() {
    document.getElementById('article-section').innerHTML = '';
}

class Beitrag {
    constructor() {
    }

    anzeigen() {
        let artikel = document.createElement('article');
        artikel.className = 'post';
        document.getElementById("content").appendChild(artikel);
    }
}

class Artikel {
    constructor(verfasser, titel, text) {
        this.verfasser = verfasser;
        this.titel = titel;
        this.text = text;
    }
}

