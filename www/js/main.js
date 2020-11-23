window.onload = function() {
    // on window loaded
}

window.onerror = function (msg, url, line) {
    bridge.log(msg, url, line);
}

function Sleep(milliseconds) {
    return new Promise(resolve => setTimeout(resolve, milliseconds));
}

function fill(art) {
    document.getElementById("ti1").innerHTML = art["titel"];
    document.getElementById("te1").innerHTML = art["text"];
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