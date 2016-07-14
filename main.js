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

function position_in_two_cols(es) {
    var i, e;
    var leftTop = 0, rightTop = 0;
    var leftLeft = 0, rightLeft = horizontalSpan(es[0]);

    makeParentPositionRelative(es[0]);

    for (i = 0; i < es.length; i++) {
        e = es[i];
        e.style.position = "absolute";
        if (leftTop <= rightTop) {
            e.style.top = "" + leftTop + "px";
            e.style.left = "" + leftLeft + "px";
            leftTop += verticalSpan(e);
        } else {
            e.style.top = "" + rightTop + "px";
            e.style.left = "" + rightLeft + "px";
            rightTop += verticalSpan(e);
        }
    }
}

window.addEventListener("load", function (event) {
    var entries = document.getElementsByClassName("entry");

    if (document.body.offsetWidth >= 2*horizontalSpan(entries[0]))
        position_in_two_cols(entries);
});
