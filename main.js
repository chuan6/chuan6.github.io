function makeParentPositionRelative(elmt) {
    elmt.parentNode.style.position = "relative";
}

function horizontalSpan(elmt) {
    var computed = window.getComputedStyle(elmt);
    return parseFloat(computed.marginLeft)
        + elmt.offsetWidth
        + parseFloat(computed.marginRight);
}

function verticalSpan(elmt) {
    var computed = window.getComputedStyle(elmt);
    return parseFloat(computed.marginTop)
        + elmt.offsetHeight
        + parseFloat(computed.marginBottom);
}

function px(x) {
    return "" + x + "px";
}

function absolutePosition(es) {
    var i;

    makeParentPositionRelative(es[0]);

    for (i = 0; i < es.length; i++) {
        es[i].style.position = "absolute";
    }
}

function leftMostMin(xs) {
    var tmp = [xs[0], 0];
    var i;

    for (i = 1; i < xs.length; i++) {
        if (xs[i] < tmp[0]) {
            tmp = [xs[i], i];
        }
    }
    return tmp;
}

function numOfCols(e, containerWidth) {
    return Math.floor(containerWidth / horizontalSpan(e));
}

// position entries es in n columns, in the given container
function dynamicPositioning(container, es, n) {
    console.log("dynamicPositioning ", n, " columns");
    var i, e;
    var tops = [];
    var hspan = horizontalSpan(es[0]);
    var top, idx;

    container.style.width = px(hspan * n);

    tops.length = n;
    for (i = 0; i < tops.length; i++) {
        tops[i] = 0;
    }

    for (i = 0; i < es.length; i++) {
        e = es[i];
        [top, idx] = leftMostMin(tops);
        e.style.top = px(top);
        e.style.left = px(hspan * idx);
        tops[idx] += verticalSpan(e);
    }
}

window.addEventListener("load", function () {
    var container = document.getElementById("entries-container");
    var entries = document.getElementsByClassName("entry");
    var ncols;

    absolutePosition(entries);
    ncols = numOfCols(entries[0], document.body.offsetWidth);
    dynamicPositioning(container, entries, ncols);

    window.onresize = function () {
        var n = numOfCols(entries[0], document.body.offsetWidth);
        if (n !== ncols) {
            dynamicPositioning(container, entries, n);
            ncols = n;
        }
    };
});
