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

    for (i = 0; i < es.length; i++) {
        es[i].style.position = "absolute";
    }
}

function numOfCols(e, containerWidth) {
    return Math.floor(containerWidth / horizontalSpan(e));
}

function topValues(n) {
    var tv = [];
    tv.length = n;

    return {
        fill: function (x) {
            var i;
            for (i = 0; i < tv.length; i++) {
                tv[i] = 0
            }
        },
        increment: function (i, x) {
            if (i >= 0 && i < tv.length) tv[i] += x;
        },
        leftMostMin: function () {
            var min = [tv[0], 0];
            var i;

            for (i = 1; i < tv.length; i++) {
                if (tv[i] < min[0]) {
                    min = [tv[i], i];
                }
            }
            return min;
        }
    };
}

// position entries es in n columns, in the given container
function dynamicPositioning(container, es, n) {
    console.log("dynamicPositioning ", n, " columns");
    var i, e;
    var tops = topValues(n);
    var hspan = horizontalSpan(es[0]);
    var top, idx, pair;

    container.style.width = px(hspan * n);
    tops.fill(0);

    for (i = 0; i < es.length; i++) {
        e = es[i];
        pair = tops.leftMostMin();
        top = pair[0];
        idx = pair[1];
        e.style.top = px(top);
        e.style.left = px(hspan * idx);
        tops.increment(idx, verticalSpan(e));
    }
}

function elmtYOffset(e) {
    var x, r = 0;

    for (x = e; x; x = x.offsetParent) {
        //console.log("x.offsetTop: ", x.offsetTop);
        r += x.offsetTop;
        //console.log("r: ", r);
    }
    return r;
}

function entryTitleOnScroll(e) {
    var title = e.querySelector(".title");
    var fixed = title.cloneNode(true);

    fixed.style.position = "fixed";
    fixed.style.top = "0";

    return function () {
        var pageY = window.pageYOffset;
        var elmtY = elmtYOffset(title);
        var shouldBeFixed = pageY > elmtY && pageY < elmtY+e.offsetHeight;
        var isFixed = !!fixed.parentNode;

        if (isFixed !== shouldBeFixed) {
            if (shouldBeFixed) {//fixed is undefined
                fixed = e.insertBefore(fixed, title);
            } else {
                e.removeChild(fixed);
            }
        }
    };
}

window.addEventListener("load", function () {
    var container = document.getElementById("entries-container");
    var entries = document.getElementsByClassName("entry");
    var i, ncols;

    container.style.position = "relative";
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

    for (i = 0; i < entries.length; i++) {
        window.addEventListener("scroll", entryTitleOnScroll(entries[i]));
    }
});
