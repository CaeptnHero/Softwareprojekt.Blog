window.onload = function() {
    // on window loaded
    clearArticles();
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
    for (i = 1; i <=number; i++ ) {
        var node = document.createElement("li");
        var textnode = document.createTextNode(i);
        node.id = i;
        node.appendChild(textnode);
        document.querySelector("footer > nav > ul").appendChild(node);
        document.getElementById(i).innerHTML = "<li onclick='bridge.fillWeb(" + i + ")'><a>" + i + "</a></li>" ; // Anpassen sobald das Layout fertig. Als Beispiel <div class="message">Add Message<br>Title: <input type="text usw.

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
    article.id = ID;
    article.innerHTML =
        `<h2 id="titel2">${Title}</h2>
         <p id="text2">${Text}</p>
         <button class="button button1">Kommentieren</button>
         <button class="button button2">Löschen</button>`;

    document.getElementById("article-section").appendChild(article);
}

//Funktion zum hinzufügen von kommentaren in webview & in die DB
function addKommentar(beitragID, kommentarText) {
    document.getElementById(beitragID.toString()).app
}

function deleteArticle(id) {
    //TODO: kommentar aus view & DB löschen
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

