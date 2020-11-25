window.onload = function() {
    // on window loaded
}

window.onerror = function (msg, url, line) {
    bridge.errorLog(msg, url, line);
}



/*
function fill(art) {
    document.getElementById("ti1").innerHTML = art["titel"];
    document.getElementById("te1").innerHTML = art["text"];
}
*/

function fill(art, s) {
    document.getElementById("titel"+ s).innerHTML = art["titel"];
    document.getElementById("text" + s).innerHTML = art["text"];
}

function ready(){
    addP();
}

//Hilfsfunktion um die Seitenanzahl aus Java per Bride zu verwenden
function addP(){
    bridge.addPage();
}

// Funktion um weitere Seiten hinzuzufügen
function Page(number){

    for (i = 1; i <=number; i++ ) {
        var node = document.createElement("LI");
        var textnode = document.createTextNode(i);
        node.id = i;
        node.appendChild(textnode);
        document.getElementById("page").appendChild(node);
        document.getElementById(i).innerHTML = i; // Anpassen sobald das Layout fertig. Als Beispiel <div class="message">Add Message<br>Title: <input type="text usw.
    }
}

function createArticle() {
    let title = document.querySelector('input[name="tname"]').value;
    let text = document.querySelector('input[name="ttext"]').value;
    bridge.createArticle(title, text);
}

//Funktion zum anzeigen von Artikeln aus DB
function displayArticle(id, title, text) {
    //TODO: Artikel element erstellen und mit übergebenen werten füllen
}

//Funktion zum hinzufügen von kommentaren in webview & in die DB
function addKommentar(beitragID, kommentarText) {
    //TODO: kommentar einen beitrag hinzufügen
}

function deleteArticle(id) {
    //TODO: kommentar aus view & DB löschen
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

