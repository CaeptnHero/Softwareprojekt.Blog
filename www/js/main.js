window.onload = function() {
    // on window loaded
    clearArticles();
    addDeleteEventHandler();
}

window.onerror = function (msg, url, line) {
    bridge.errorLog(msg, url, line);
}

function fill(art, s) {
    // document.getElementById("titel"+ s).innerHTML = art["titel"];
    // document.getElementById("text" + s).innerHTML = art["text"];

    //FIXME: tmp fix
    displayArticle(-1, art.titel, art.text);
}

function ready(){
    addP();
}

//Hilfsfunktion um die Seitenanzahl aus Java per Bride zu verwenden
function addP(){
    bridge.addPage();
    bridge.fillWeb(1);
}

// Funktion um weitere Seiten hinzuzufügen
function Page(number){
    var pageNav = document.querySelector("footer > nav > ul");
    for (i = 1; i <=number; i++ ) {
        var node = document.createElement("li");
        node.onclick = "bridge.fillWeb(" + i + ")";
        node.innerHTML = "<a href='#'>" + i + "</a>";
        pageNav.appendChild(node) //TODO: Anpassen sobald das Layout fertig. Als Beispiel <div class="message">Add Message<br>Title: <input type="text usw.
    }
}

function createArticle() {
    let title = document.getElementById('article-title').value;
    let text = document.getElementById('article-text').value;
    bridge.createArticle(title, text);
}

//Funktion zum anzeigen von Artikeln aus DB
function displayArticle(ID, Title, Text) {
    let article = document.createElement('article');
    article.id = `beitrag-${ID}`;
    article.innerHTML =
        `<h2>${Title}</h2>
         <p>${Text}</p>
         <div class="post-actions">
            <button>Kommentieren</button> <button class="post-delete">Löschen</button>
         </div>
        <div class="comments"></div>`;

    document.getElementById("article-section").appendChild(article);
}

/**
 * Fügt in der webview einen Beitrag(Artikel/Kommentar) ein Kommentar hinzu
 * @param beitragID
 * @param kommentarID
 * @param verfasser
 * @param kommentarText
 */
function addKommentar(beitragID, kommentarID, verfasser, kommentarText) {
    let comment = document.createElement('div');
    comment.id = `beitrag-${kommentarID}`;
    comment.innerHTML = `<span class="username">${verfasser}</span>
                    <p>${kommentarText}</p>
                    <div class="post-actions">
                        <button>Kommentieren</button> <button class="post-delete">Löschen</button>
                    </div>
                    <div class="comments"></div>`;

    document.querySelector(`#beitrag-${beitragID} .comments`).appendChild(comment);
}

/**
 * Löscht 
 * @param id
 */
function deleteArticle(id) {
    document.getElementById(id).remove();
}

function addDeleteEventHandler() {
    var nodes = document.querySelectorAll('.post-delete');
    nodes.forEach((element) => {
        element.addEventListener('click', function () {
            deleteArticle(this.parentElement.parentElement.id);
        });
    });
}

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

