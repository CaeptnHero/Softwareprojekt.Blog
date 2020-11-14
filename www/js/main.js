window.onload = function() {
    
    test()
}

async function test() {
    while (true) {
        var h1 = this.document.querySelector('h1');
        h1.style.cssText = "color: #" + Math.floor(Math.random()*16777215).toString(16);
        await Sleep(1000);
    }
}

function uebergabe(art) {
    document.getElementById("p1").innerHTML = art["titel"] + ", " + art["text"];
}

function Sleep(milliseconds) {
    return new Promise(resolve => setTimeout(resolve, milliseconds));
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