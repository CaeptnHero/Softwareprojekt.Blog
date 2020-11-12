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

function Sleep(milliseconds) {
    return new Promise(resolve => setTimeout(resolve, milliseconds));
}

