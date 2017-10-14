function () {
    return function () {
        function i(a) {
            throw a;
        }

        var n = void 0, o = null, q, r = this;

        function aa() {
        }

        function ba(a) {
            var b = typeof a;
            if (b == "object")if (a) {
                if (a instanceof Array)return"array"; else if (a instanceof Object)return b;
                var c = Object.prototype.toString.call(a);
                if (c == "[object Window]")return"object";
                if (c == "[object Array]" || typeof a.length == "number" && typeof a.splice != "undefined" && typeof a.propertyIsEnumerable != "undefined" && !a.propertyIsEnumerable("splice"))return"array";
                if (c == "[object Function]" || typeof a.call != "undefined" && typeof a.propertyIsEnumerable != "undefined" && !a.propertyIsEnumerable("call"))return"function"
            } else return"null";
            else if (b == "function" && typeof a.call == "undefined")return"object";
            return b
        }

        function s(a) {
            return typeof a == "string"
        }

        function t(a) {
            return ba(a) == "function"
        }

        function ca(a) {
            a = ba(a);
            return a == "object" || a == "array" || a == "function"
        }

        function da(a) {
            return a[ea] || (a[ea] = ++fa)
        }

        var ea = "closure_uid_" + Math.floor(Math.random() * 2147483648).toString(36), fa = 0, ga = Date.now || function () {
            return+new Date
        };

        function u(a, b) {
            function c() {
            }

            c.prototype = b.prototype;
            a.t = b.prototype;
            a.prototype = new c
        };
        function ha(a, b) {
            var c = a.length - b.length;
            return c >= 0 && a.indexOf(b, c) == c
        }

        function ia(a) {
            for (var b = 1; b < arguments.length; b++)var c = String(arguments[b]).replace(/\$/g, "$$$$"), a = a.replace(/\%s/, c);
            return a
        }

        function v(a) {
            return a.replace(/^[\s\xa0]+|[\s\xa0]+$/g, "")
        }

        function ja(a, b) {
            for (var c = 0, d = v(String(a)).split("."), e = v(String(b)).split("."), f = Math.max(d.length, e.length), g = 0; c == 0 && g < f; g++) {
                var h = d[g] || "", j = e[g] || "", l = RegExp("(\\d*)(\\D*)", "g"), m = RegExp("(\\d*)(\\D*)", "g");
                do {
                    var k = l.exec(h) || ["", "", ""], p = m.exec(j) || ["", "", ""];
                    if (k[0].length == 0 && p[0].length == 0)break;
                    c = ka(k[1].length == 0 ? 0 : parseInt(k[1], 10), p[1].length == 0 ? 0 : parseInt(p[1], 10)) || ka(k[2].length == 0, p[2].length == 0) || ka(k[2], p[2])
                } while (c == 0)
            }
            return c
        }

        function ka(a, b) {
            if (a < b)return-1; else if (a > b)return 1;
            return 0
        }

        var la = {};

        function ma(a) {
            return la[a] || (la[a] = String(a).replace(/\-([a-z])/g, function (a, c) {
                return c.toUpperCase()
            }))
        };
        var w, na, oa, pa;

        function qa() {
            return r.navigator ? r.navigator.userAgent : o
        }

        pa = oa = na = w = !1;
        var ra;
        if (ra = qa()) {
            var sa = r.navigator;
            w = ra.indexOf("Opera") == 0;
            na = !w && ra.indexOf("MSIE") != -1;
            oa = !w && ra.indexOf("WebKit") != -1;
            pa = !w && !oa && sa.product == "Gecko"
        }
        var ta = w, x = na, y = pa, z = oa, ua = r.navigator, va = (ua && ua.platform || "").indexOf("Mac") != -1, wa;
        a:{
            var xa = "", A;
            if (ta && r.opera)var ya = r.opera.version, xa = typeof ya == "function" ? ya() : ya; else if (y ? A = /rv\:([^\);]+)(\)|;)/ : x ? A = /MSIE\s+([^\);]+)(\)|;)/ : z && (A = /WebKit\/(\S+)/), A)var za = A.exec(qa()), xa = za ? za[1] : "";
            if (x) {
                var Aa, Ba = r.document;
                Aa = Ba ? Ba.documentMode : n;
                if (Aa > parseFloat(xa)) {
                    wa = String(Aa);
                    break a
                }
            }
            wa = xa
        }
        var Ca = {};

        function B(a) {
            return Ca[a] || (Ca[a] = ja(wa, a) >= 0)
        };
        var C = window;

        function D(a) {
            this.stack = Error().stack || "";
            if (a)this.message = String(a)
        }

        u(D, Error);
        D.prototype.name = "CustomError";
        function Da(a, b) {
            b.unshift(a);
            D.call(this, ia.apply(o, b));
            b.shift();
            this.ba = a
        }

        u(Da, D);
        Da.prototype.name = "AssertionError";
        function Ea(a, b) {
            if (!a) {
                var c = Array.prototype.slice.call(arguments, 2), d = "Assertion failed";
                if (b) {
                    d += ": " + b;
                    var e = c
                }
                i(new Da("" + d, e || []))
            }
        };
        var Fa = Array.prototype;

        function E(a, b) {
            if (s(a)) {
                if (!s(b) || b.length != 1)return-1;
                return a.indexOf(b, 0)
            }
            for (var c = 0; c < a.length; c++)if (c in a && a[c] === b)return c;
            return-1
        }

        function Ga(a, b) {
            for (var c = a.length, d = s(a) ? a.split("") : a, e = 0; e < c; e++)e in d && b.call(n, d[e], e, a)
        }

        function G(a, b) {
            for (var c = a.length, d = [], e = 0, f = s(a) ? a.split("") : a, g = 0; g < c; g++)if (g in f) {
                var h = f[g];
                b.call(n, h, g, a) && (d[e++] = h)
            }
            return d
        }

        function Ha(a, b) {
            for (var c = a.length, d = s(a) ? a.split("") : a, e = 0; e < c; e++)if (e in d && b.call(n, d[e], e, a))return!0;
            return!1
        }

        function Ia(a, b) {
            var c;
            a:{
                c = a.length;
                for (var d = s(a) ? a.split("") : a, e = 0; e < c; e++)if (e in d && b.call(n, d[e], e, a)) {
                    c = e;
                    break a
                }
                c = -1
            }
            return c < 0 ? o : s(a) ? a.charAt(c) : a[c]
        };
        var Ja;
        !x || B("9");
        !y && !x || x && B("9") || y && B("1.9.1");
        x && B("9");
        function Ka(a, b) {
            this.x = a !== n ? a : 0;
            this.y = b !== n ? b : 0
        }

        Ka.prototype.toString = function () {
            return"(" + this.x + ", " + this.y + ")"
        };
        function La(a, b) {
            this.width = a;
            this.height = b
        }

        La.prototype.toString = function () {
            return"(" + this.width + " x " + this.height + ")"
        };
        La.prototype.floor = function () {
            this.width = Math.floor(this.width);
            this.height = Math.floor(this.height);
            return this
        };
        function H(a) {
            return a ? new Ma(I(a)) : Ja || (Ja = new Ma)
        }

        function Na(a, b, c, d) {
            a = d || a;
            b = b && b != "*" ? b.toUpperCase() : "";
            if (a.querySelectorAll && a.querySelector && (!z || document.compatMode == "CSS1Compat" || B("528")) && (b || c))return a.querySelectorAll(b + (c ? "." + c : ""));
            if (c && a.getElementsByClassName)if (a = a.getElementsByClassName(c), b) {
                for (var d = {}, e = 0, f = 0, g; g = a[f]; f++)b == g.nodeName && (d[e++] = g);
                d.length = e;
                return d
            } else return a;
            a = a.getElementsByTagName(b || "*");
            if (c) {
                d = {};
                for (f = e = 0; g = a[f]; f++)b = g.className, typeof b.split == "function" && E(b.split(/\s+/), c) >= 0 && (d[e++] = g);
                d.length = e;
                return d
            } else return a
        }

        function Oa(a, b) {
            if (a.contains && b.nodeType == 1)return a == b || a.contains(b);
            if (typeof a.compareDocumentPosition != "undefined")return a == b || Boolean(a.compareDocumentPosition(b) & 16);
            for (; b && a != b;)b = b.parentNode;
            return b == a
        }

        function I(a) {
            return a.nodeType == 9 ? a : a.ownerDocument || a.document
        }

        function Pa(a, b) {
            var c = [];
            return Qa(a, b, c, !0) ? c[0] : n
        }

        function Qa(a, b, c, d) {
            if (a != o)for (var e = 0, f; f = a.childNodes[e]; e++) {
                if (b(f) && (c.push(f), d))return!0;
                if (Qa(f, b, c, d))return!0
            }
            return!1
        }

        function Ra(a, b) {
            for (var a = a.parentNode, c = 0; a;) {
                if (b(a))return a;
                a = a.parentNode;
                c++
            }
            return o
        }

        function Ma(a) {
            this.o = a || r.document || document
        }

        function J(a, b, c, d) {
            return Na(a.o, b, c, d)
        }

        function Sa(a) {
            var b = a.o, a = !z && b.compatMode == "CSS1Compat" ? b.documentElement : b.body, b = b.parentWindow || b.defaultView;
            return new Ka(b.pageXOffset || a.scrollLeft, b.pageYOffset || a.scrollTop)
        }

        Ma.prototype.contains = Oa;
        var Ta = {B: function (a, b, c) {
            var d = I(a);
            if (!d.implementation.hasFeature("XPath", "3.0"))return o;
            var e = d.createNSResolver(d.documentElement);
            return d.evaluate(b, a, e, c, o)
        }, b: function (a, b) {
            var c = function (b, c) {
                var f = I(b);
                if (b.selectSingleNode)return f.setProperty && f.setProperty("SelectionLanguage", "XPath"), b.selectSingleNode(c);
                try {
                    var g = Ta.B(b, c, 9);
                    return g ? g.singleNodeValue : o
                } catch (h) {
                    i(Error(Ua, "Unable to locate an element with the xpath expression " + a))
                }
            }(b, a);
            if (!c)return o;
            c.nodeType != 1 && i(Error("Returned node is not an element: " +
                    a));
            return c
        }, g: function (a, b) {
            var c = function (b, c) {
                var f = I(b);
                if (b.selectNodes)return f.setProperty && f.setProperty("SelectionLanguage", "XPath"), b.selectNodes(c);
                var f = [], g;
                try {
                    g = Ta.B(b, c, 7)
                } catch (h) {
                    i(Error(Ua, "Unable to locate elements with the xpath expression " + c))
                }
                if (g)for (var j = g.snapshotLength, l = 0; l < j; ++l) {
                    var m = g.snapshotItem(l);
                    m.nodeType != 1 && i(Error(Ua, "Returned nodes must be elements: " + a));
                    f.push(m)
                }
                return f
            }(b, a);
            Ga(c, function (b) {
                b.nodeType != 1 && i(Error("Returned nodes must be elements: " +
                        a))
            });
            return c
        }};
        var Va = "StopIteration"in r ? r.StopIteration : Error("StopIteration");

        function Wa() {
        }

        Wa.prototype.next = function () {
            i(Va)
        };
        function K(a, b, c, d, e) {
            this.a = !!b;
            a && L(this, a, d);
            this.m = e != n ? e : this.e || 0;
            this.a && (this.m *= -1);
            this.P = !c
        }

        u(K, Wa);
        q = K.prototype;
        q.d = o;
        q.e = 0;
        q.L = !1;
        function L(a, b, c) {
            if (a.d = b)a.e = typeof c == "number" ? c : a.d.nodeType != 1 ? 0 : a.a ? -1 : 1
        }

        q.next = function () {
            var a;
            if (this.L) {
                (!this.d || this.P && this.m == 0) && i(Va);
                a = this.d;
                var b = this.a ? -1 : 1;
                if (this.e == b) {
                    var c = this.a ? a.lastChild : a.firstChild;
                    c ? L(this, c) : L(this, a, b * -1)
                } else(c = this.a ? a.previousSibling : a.nextSibling) ? L(this, c) : L(this, a.parentNode, b * -1);
                this.m += this.e * (this.a ? -1 : 1)
            } else this.L = !0;
            (a = this.d) || i(Va);
            return a
        };
        q.splice = function () {
            var a = this.d, b = this.a ? 1 : -1;
            if (this.e == b)this.e = b * -1, this.m += this.e * (this.a ? -1 : 1);
            this.a = !this.a;
            K.prototype.next.call(this);
            this.a = !this.a;
            for (var b = arguments[0], c = ba(b), b = c == "array" || c == "object" && typeof b.length == "number" ? arguments[0] : arguments, c = b.length - 1; c >= 0; c--)a.parentNode && a.parentNode.insertBefore(b[c], a.nextSibling);
            a && a.parentNode && a.parentNode.removeChild(a)
        };
        function Xa(a, b, c, d) {
            K.call(this, a, b, c, o, d)
        }

        u(Xa, K);
        Xa.prototype.next = function () {
            do Xa.t.next.call(this); while (this.e == -1);
            return this.d
        };
        function Ya(a, b) {
            var c = I(a);
            if (c.defaultView && c.defaultView.getComputedStyle && (c = c.defaultView.getComputedStyle(a, o)))return c[b] || c.getPropertyValue(b);
            return""
        }

        function Za(a, b) {
            return Ya(a, b) || (a.currentStyle ? a.currentStyle[b] : o) || a.style[b]
        }

        function $a(a) {
            var b = a.getBoundingClientRect();
            if (x)a = a.ownerDocument, b.left -= a.documentElement.clientLeft + a.body.clientLeft, b.top -= a.documentElement.clientTop + a.body.clientTop;
            return b
        }

        function ab(a) {
            if (x)return a.offsetParent;
            for (var b = I(a), c = Za(a, "position"), d = c == "fixed" || c == "absolute", a = a.parentNode; a && a != b; a = a.parentNode)if (c = Za(a, "position"), d = d && c == "static" && a != b.documentElement && a != b.body, !d && (a.scrollWidth > a.clientWidth || a.scrollHeight > a.clientHeight || c == "fixed" || c == "absolute" || c == "relative"))return a;
            return o
        };
        function M(a, b) {
            return!!a && a.nodeType == 1 && (!b || a.tagName.toUpperCase() == b)
        }

        var bb = {"class": "className", readonly: "readOnly"}, cb = ["checked", "disabled", "draggable", "hidden"];

        function db(a, b) {
            var c = bb[b] || b, d = a[c];
            if (d === n && E(cb, c) >= 0)return!1;
            return d
        }

        var eb = ["async", "autofocus", "autoplay", "checked", "compact", "complete", "controls", "declare", "defaultchecked", "defaultselected", "defer", "disabled", "draggable", "ended", "formnovalidate", "hidden", "indeterminate", "iscontenteditable", "ismap", "itemscope", "loop", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "paused", "pubdate", "readonly", "required", "reversed", "scoped", "seamless", "seeking", "selected", "spellcheck", "truespeed", "willvalidate"];

        function N(a, b) {
            if (8 == a.nodeType)return o;
            b = b.toLowerCase();
            if (b == "style") {
                var c = v(a.style.cssText).toLowerCase();
                return c.charAt(c.length - 1) == ";" ? c : c + ";"
            }
            c = a.getAttributeNode(b);
            x && !c && B(8) && E(eb, b) >= 0 && (c = a[b]);
            if (!c)return o;
            if (E(eb, b) >= 0)return x && c.value == "false" ? o : "true";
            return c.specified ? c.value : o
        }

        function fb(a) {
            for (a = a.parentNode; a && a.nodeType != 1 && a.nodeType != 9 && a.nodeType != 11;)a = a.parentNode;
            return M(a) ? a : o
        }

        function O(a, b) {
            b = ma(String(b));
            return Ya(a, b) || gb(a, b)
        }

        function gb(a, b) {
            var c = (a.currentStyle || a.style)[b];
            if (c != "inherit")return c !== n ? c : o;
            return(c = fb(a)) ? gb(c, b) : o
        }

        function hb(a) {
            if (t(a.getBBox))return a.getBBox();
            var b;
            if (Za(a, "display") != "none")b = new La(a.offsetWidth, a.offsetHeight); else {
                b = a.style;
                var c = b.display, d = b.visibility, e = b.position;
                b.visibility = "hidden";
                b.position = "absolute";
                b.display = "inline";
                var f = a.offsetWidth, a = a.offsetHeight;
                b.display = c;
                b.position = e;
                b.visibility = d;
                b = new La(f, a)
            }
            return b
        }

        function ib(a, b) {
            function c(a) {
                if (O(a, "display") == "none")return!1;
                a = fb(a);
                return!a || c(a)
            }

            function d(a) {
                var b = hb(a);
                if (b.height > 0 && b.width > 0)return!0;
                if (a.innerText || a.textContent)if (jb.test(a.innerText || a.textContent))return!0;
                return z && Ha(a.childNodes, function (a) {
                    return M(a) && d(a)
                })
            }

            M(a) || i(Error("Argument to isShown must be of type Element"));
            if (M(a, "TITLE"))return(I(a) ? I(a).parentWindow || I(a).defaultView : window) == C;
            if (M(a, "OPTION") || M(a, "OPTGROUP")) {
                var e = Ra(a, function (a) {
                    return M(a, "SELECT")
                });
                return!!e && ib(e, b)
            }
            if (M(a, "MAP")) {
                if (!a.name)return!1;
                e = I(a);
                e = e.evaluate ? Ta.b('/descendant::*[@usemap = "#' + a.name + '"]', e) : Pa(e, function (b) {
                    return M(b) && N(b, "usemap") == "#" + a.name
                });
                return!!e && ib(e, b)
            }
            if (M(a, "AREA"))return e = Ra(a, function (a) {
                return M(a, "MAP")
            }), !!e && ib(e, b);
            if (M(a, "INPUT") && a.type.toLowerCase() == "hidden")return!1;
            if (O(a, "visibility") == "hidden")return!1;
            if (!c(a))return!1;
            if (!b && kb(a) == 0)return!1;
            if (!d(a))return!1;
            return!0
        }

        function lb(a) {
            var b = [];
            mb(a, b);
            for (var c = b, a = c.length, b = Array(a), c = s(c) ? c.split("") : c, d = 0; d < a; d++)d in c && (b[d] = v.call(n, c[d]));
            return v(b.join("\n"))
        }

        function mb(a, b) {
            if (M(a, "BR"))b.push(""); else {
                var c = O(a, "display"), d = !(E(nb, c) >= 0);
                d && b[b.length - 1] && !jb.test(b[b.length - 1]) && b.push("");
                var e = ib(a);
                if (e)var f = O(a, "white-space"), g = O(a, "text-transform");
                Ga(a.childNodes, function (a) {
                    a.nodeType == 3 && e ? ob(a, b, f, g) : M(a) && mb(a, b)
                });
                var h = b[b.length - 1];
                c == "table-cell" && h && !ha(h, " ") && (b[b.length - 1] += " ");
                d && h && !jb.test(h) && b.push("")
            }
        }

        var nb = ["inline", "inline-block", "inline-table", "none", "table-cell", "table-column", "table-column-group"], pb = "[\\s\\xa0" + String.fromCharCode(160) + "]+", qb = RegExp(pb, "g"), jb = RegExp("^" + pb + "$");

        function ob(a, b, c, d) {
            a = a.nodeValue.replace(/(\r\n|\r|\n)/g, "\n");
            a = a.replace(/\u200b/g, "");
            c == "normal" || c == "nowrap" ? a = a.replace(qb, " ") : c == "pre-line" && (a = a.replace(/\xa0|[ \t]+/g, " "));
            a = a.replace(/\xa0|\t/g, " ");
            d == "capitalize" ? a = a.replace(/(^|\s)(\S)/g, function (a, b, c) {
                return b + c.toUpperCase()
            }) : d == "uppercase" ? a = a.toUpperCase() : d == "lowercase" && (a = a.toLowerCase());
            c = b.pop() || "";
            ha(c, " ") && a.lastIndexOf(" ", 0) == 0 && (a = a.substr(1));
            b.push(c + a)
        }

        function kb(a) {
            if (x) {
                if (O(a, "position") == "relative")return 1;
                a = O(a, "filter");
                return(a = a.match(/^alpha\(opacity=(\d*)\)/) || a.match(/^progid:DXImageTransform.Microsoft.Alpha\(Opacity=(\d*)\)/)) ? Number(a[1]) / 100 : 1
            } else return rb(a)
        }

        function rb(a) {
            var b = 1, c = O(a, "opacity");
            c && (b = Number(c));
            (a = fb(a)) && (b *= rb(a));
            return b
        };
        var sb, tb, ub, vb, wb, xb, yb;
        yb = xb = wb = vb = ub = tb = sb = !1;
        var P = qa();
        P && (P.indexOf("Firefox") != -1 ? sb = !0 : P.indexOf("Camino") != -1 ? tb = !0 : P.indexOf("iPhone") != -1 || P.indexOf("iPod") != -1 ? ub = !0 : P.indexOf("iPad") != -1 ? vb = !0 : P.indexOf("Android") != -1 ? wb = !0 : P.indexOf("Chrome") != -1 ? xb = !0 : P.indexOf("Safari") != -1 && (yb = !0));
        var zb = tb, Ab = ub, Bb = vb, Cb = wb, Db = xb, Eb = yb;
        a:{
            var Q;
            if (sb)Q = /Firefox\/([0-9.]+)/; else if (x || ta)break a; else Db ? Q = /Chrome\/([0-9.]+)/ : Eb ? Q = /Version\/([0-9.]+)/ : Ab || Bb ? Q = /Version\/(\S+).*Mobile\/(\S+)/ : Cb ? Q = /Android\s+([0-9.]+)(?:.*Version\/([0-9.]+))?/ : zb && (Q = /Camino\/([0-9.]+)/);
            Q && Q.exec(qa())
        }
        ;
        var Fb;
        var Gb = ["dragstart", "dragexit", "mouseover", "mouseout"];

        function R(a, b, c) {
            var d = I(a), e = d ? d.parentWindow || d.defaultView : window, f = new Ka;
            if (a.nodeType == 1)if (a.getBoundingClientRect) {
                var g = $a(a);
                f.x = g.left;
                f.y = g.top
            } else {
                g = Sa(H(a));
                var h, j = I(a), l = Za(a, "position"), m = y && j.getBoxObjectFor && !a.getBoundingClientRect && l == "absolute" && (h = j.getBoxObjectFor(a)) && (h.screenX < 0 || h.screenY < 0), k = new Ka(0, 0), p;
                h = j ? j.nodeType == 9 ? j : I(j) : document;
                if (p = x)if (p = !B(9))p = H(h).o.compatMode != "CSS1Compat";
                p = p ? h.body : h.documentElement;
                if (a != p)if (a.getBoundingClientRect)h = $a(a), j = Sa(H(j)),
                        k.x = h.left + j.x, k.y = h.top + j.y; else if (j.getBoxObjectFor && !m)h = j.getBoxObjectFor(a), j = j.getBoxObjectFor(p), k.x = h.screenX - j.screenX, k.y = h.screenY - j.screenY; else {
                    m = a;
                    do {
                        k.x += m.offsetLeft;
                        k.y += m.offsetTop;
                        m != a && (k.x += m.clientLeft || 0, k.y += m.clientTop || 0);
                        if (z && Za(m, "position") == "fixed") {
                            k.x += j.body.scrollLeft;
                            k.y += j.body.scrollTop;
                            break
                        }
                        m = m.offsetParent
                    } while (m && m != a);
                    if (ta || z && l == "absolute")k.y -= j.body.offsetTop;
                    for (m = a; (m = ab(m)) && m != j.body && m != p;)if (k.x -= m.scrollLeft, !ta || m.tagName != "TR")k.y -= m.scrollTop
                }
                f.x =
                        k.x - g.x;
                f.y = k.y - g.y
            } else g = t(a.Q), k = a, a.targetTouches ? k = a.targetTouches[0] : g && a.j.targetTouches && (k = a.j.targetTouches[0]), f.x = k.clientX, f.y = k.clientY;
            p = c || {};
            c = (p.x || 0) + f.x;
            f = (p.y || 0) + f.y;
            g = p.button || 0;
            k = p.bubble || !0;
            j = o;
            E(Gb, b) >= 0 && (j = p.related || o);
            l = !!p.alt;
            m = !!p.control;
            h = !!p.shift;
            p = !!p.meta;
            a.fireEvent && d && d.createEventObject ? (a = d.createEventObject(), a.altKey = l, a.Y = m, a.metaKey = p, a.shiftKey = h, a.clientX = c, a.clientY = f, a.button = g, a.relatedTarget = j) : (a = d.createEvent("MouseEvents"), a.initMouseEvent ?
                    a.initMouseEvent(b, k, !0, e, 1, 0, 0, c, f, m, l, h, p, g, j) : (a.initEvent(b, k, !0), a.shiftKey = h, a.metaKey = p, a.altKey = l, a.ctrlKey = m, a.button = g));
            return a
        }

        function Hb(a, b, c) {
            var d = I(a), a = d ? d.parentWindow || d.defaultView : window, e = c || {}, c = e.keyCode || 0, f = e.charCode || 0, g = !!e.alt, h = !!e.ctrl, j = !!e.shift, e = !!e.meta;
            y ? (d = d.createEvent("KeyboardEvent"), d.initKeyEvent(b, !0, !0, a, h, g, j, e, c, f)) : (x ? d = d.createEventObject() : (d = d.createEvent("Events"), d.initEvent(b, !0, !0), d.charCode = f), d.keyCode = c, d.altKey = g, d.ctrlKey = h, d.metaKey = e, d.shiftKey = j);
            return d
        }

        function Ib(a, b, c) {
            var d = I(a), e = c || {}, c = e.bubble !== !1, f = !!e.alt, g = !!e.control, h = !!e.shift, e = !!e.meta;
            a.fireEvent && d && d.createEventObject ? (a = d.createEventObject(), a.altKey = f, a.Z = g, a.metaKey = e, a.shiftKey = h) : (a = d.createEvent("HTMLEvents"), a.initEvent(b, c, !0), a.shiftKey = h, a.metaKey = e, a.altKey = f, a.ctrlKey = g);
            return a
        }

        var S = {};
        S.click = R;
        S.keydown = Hb;
        S.keypress = Hb;
        S.keyup = Hb;
        S.mousedown = R;
        S.mousemove = R;
        S.mouseout = R;
        S.mouseover = R;
        S.mouseup = R;
        function T(a) {
            D.call(this, a)
        }

        u(T, D);
        var Ua = 32;

        function Jb(a, b) {
            D.call(this, b);
            this.code = a;
            this.name = Kb[a] || Kb[13]
        }

        u(Jb, D);
        var Kb, Lb = {NoSuchElementError: 7, NoSuchFrameError: 8, UnknownCommandError: 9, StaleElementReferenceError: 10, ElementNotVisibleError: 11, InvalidElementStateError: 12, UnknownError: 13, ElementNotSelectableError: 15, XPathLookupError: 19, NoSuchWindowError: 23, InvalidCookieDomainError: 24, UnableToSetCookieError: 25, ModalDialogOpenedError: 26, ModalDialogOpenError: 27, ScriptTimeoutError: 28}, Mb = {}, Nb;
        for (Nb in Lb)Mb[Lb[Nb]] = Nb;
        Kb = Mb;
        Jb.prototype.toString = function () {
            return"[" + this.name + "] " + this.message
        };
        !x || B("9");
        x && B("8");
        function U() {
            Ob && (Pb[da(this)] = this)
        }

        var Ob = !1, Pb = {};
        U.prototype.z = !1;
        U.prototype.n = function () {
            if (!this.z && (this.z = !0, this.i(), Ob)) {
                var a = da(this);
                Pb.hasOwnProperty(a) || i(Error(this + " did not call the goog.Disposable base constructor or was disposed of after a clearUndisposedObjects call"));
                delete Pb[a]
            }
        };
        U.prototype.i = function () {
        };
        function V(a, b) {
            U.call(this);
            this.type = a;
            this.currentTarget = this.target = b
        }

        u(V, U);
        V.prototype.i = function () {
            delete this.type;
            delete this.target;
            delete this.currentTarget
        };
        V.prototype.s = !1;
        V.prototype.W = !0;
        var Qb = new Function("a", "return a");

        function Rb(a, b) {
            a && this.q(a, b)
        }

        u(Rb, V);
        q = Rb.prototype;
        q.target = o;
        q.relatedTarget = o;
        q.offsetX = 0;
        q.offsetY = 0;
        q.clientX = 0;
        q.clientY = 0;
        q.screenX = 0;
        q.screenY = 0;
        q.button = 0;
        q.keyCode = 0;
        q.charCode = 0;
        q.ctrlKey = !1;
        q.altKey = !1;
        q.shiftKey = !1;
        q.metaKey = !1;
        q.V = !1;
        q.j = o;
        q.q = function (a, b) {
            var c = this.type = a.type;
            V.call(this, c);
            this.target = a.target || a.srcElement;
            this.currentTarget = b;
            var d = a.relatedTarget;
            if (d) {
                if (y) {
                    var e;
                    a:{
                        try {
                            Qb(d.nodeName);
                            e = !0;
                            break a
                        } catch (f) {
                        }
                        e = !1
                    }
                    e || (d = o)
                }
            } else if (c == "mouseover")d = a.fromElement; else if (c == "mouseout")d = a.toElement;
            this.relatedTarget = d;
            this.offsetX = a.offsetX !== n ? a.offsetX : a.layerX;
            this.offsetY = a.offsetY !== n ? a.offsetY : a.layerY;
            this.clientX = a.clientX !== n ? a.clientX : a.pageX;
            this.clientY = a.clientY !== n ? a.clientY : a.pageY;
            this.screenX =
                    a.screenX || 0;
            this.screenY = a.screenY || 0;
            this.button = a.button;
            this.keyCode = a.keyCode || 0;
            this.charCode = a.charCode || (c == "keypress" ? a.keyCode : 0);
            this.ctrlKey = a.ctrlKey;
            this.altKey = a.altKey;
            this.shiftKey = a.shiftKey;
            this.metaKey = a.metaKey;
            this.V = va ? a.metaKey : a.ctrlKey;
            this.state = a.state;
            this.j = a;
            delete this.W;
            delete this.s
        };
        q.Q = function () {
            return this.j
        };
        q.i = function () {
            Rb.t.i.call(this);
            this.relatedTarget = this.currentTarget = this.target = this.j = o
        };
        function Sb() {
        }

        var Tb = 0;
        q = Sb.prototype;
        q.key = 0;
        q.l = !1;
        q.u = !1;
        q.q = function (a, b, c, d, e, f) {
            t(a) ? this.D = !0 : a && a.handleEvent && t(a.handleEvent) ? this.D = !1 : i(Error("Invalid listener argument"));
            this.r = a;
            this.J = b;
            this.src = c;
            this.type = d;
            this.capture = !!e;
            this.R = f;
            this.u = !1;
            this.key = ++Tb;
            this.l = !1
        };
        q.handleEvent = function (a) {
            if (this.D)return this.r.call(this.R || this.src, a);
            return this.r.handleEvent.call(this.r, a)
        };
        function W(a, b) {
            U.call(this);
            this.G = b;
            this.f = [];
            a > this.G && i(Error("[goog.structs.SimplePool] Initial cannot be greater than max"));
            for (var c = 0; c < a; c++)this.f.push(this.c ? this.c() : {})
        }

        u(W, U);
        W.prototype.c = o;
        W.prototype.w = o;
        W.prototype.getObject = function () {
            if (this.f.length)return this.f.pop();
            return this.c ? this.c() : {}
        };
        function Ub(a, b) {
            a.f.length < a.G ? a.f.push(b) : Vb(a, b)
        }

        function Vb(a, b) {
            if (a.w)a.w(b); else if (ca(b))if (t(b.n))b.n(); else for (var c in b)delete b[c]
        }

        W.prototype.i = function () {
            W.t.i.call(this);
            for (var a = this.f; a.length;)Vb(this, a.pop());
            delete this.f
        };
        var Wb, Xb = (Wb = "ScriptEngine"in r && r.ScriptEngine() == "JScript") ? r.ScriptEngineMajorVersion() + "." + r.ScriptEngineMinorVersion() + "." + r.ScriptEngineBuildVersion() : "0";
        var Yb, Zb, $b, ac, bc, cc, dc, ec;
        (function () {
            function a() {
                return{h: 0, k: 0}
            }

            function b() {
                return[]
            }

            function c() {
                function a(b) {
                    return g.call(a.src, a.key, b)
                }

                return a
            }

            function d() {
                return new Sb
            }

            function e() {
                return new Rb
            }

            var f = Wb && !(ja(Xb, "5.7") >= 0), g;
            ac = function (a) {
                g = a
            };
            if (f) {
                Yb = function (a) {
                    Ub(h, a)
                };
                Zb = function () {
                    return j.getObject()
                };
                $b = function (a) {
                    Ub(j, a)
                };
                bc = function () {
                    Ub(l, c())
                };
                cc = function (a) {
                    Ub(m, a)
                };
                dc = function () {
                    return k.getObject()
                };
                ec = function (a) {
                    Ub(k, a)
                };
                var h = new W(0, 600);
                h.c = a;
                var j = new W(0, 600);
                j.c = b;
                var l = new W(0, 600);
                l.c = c;
                var m = new W(0, 600);
                m.c = d;
                var k = new W(0, 600);
                k.c = e
            } else Yb = aa, Zb = b, cc = bc = $b = aa, dc = e, ec = aa
        })();
        var fc = {}, X = {}, gc = {}, hc = {};

        function ic(a, b, c, d) {
            if (!d.p && d.H) {
                for (var e = 0, f = 0; e < d.length; e++)if (d[e].l) {
                    var g = d[e].J;
                    g.src = o;
                    bc(g);
                    cc(d[e])
                } else e != f && (d[f] = d[e]), f++;
                d.length = f;
                d.H = !1;
                f == 0 && ($b(d), delete X[a][b][c], X[a][b].h--, X[a][b].h == 0 && (Yb(X[a][b]), delete X[a][b], X[a].h--), X[a].h == 0 && (Yb(X[a]), delete X[a]))
            }
        }

        function jc(a) {
            if (a in hc)return hc[a];
            return hc[a] = "on" + a
        }

        function kc(a, b, c, d, e) {
            var f = 1, b = da(b);
            if (a[b]) {
                a.k--;
                a = a[b];
                a.p ? a.p++ : a.p = 1;
                try {
                    for (var g = a.length, h = 0; h < g; h++) {
                        var j = a[h];
                        j && !j.l && (f &= lc(j, e) !== !1)
                    }
                } finally {
                    a.p--, ic(c, d, b, a)
                }
            }
            return Boolean(f)
        }

        function lc(a, b) {
            var c = a.handleEvent(b);
            if (a.u) {
                var d = a.key;
                if (fc[d]) {
                    var e = fc[d];
                    if (!e.l) {
                        var f = e.src, g = e.type, h = e.J, j = e.capture;
                        f.removeEventListener ? (f == r || !f.$) && f.removeEventListener(g, h, j) : f.detachEvent && f.detachEvent(jc(g), h);
                        f = da(f);
                        h = X[g][j][f];
                        if (gc[f]) {
                            var l = gc[f], m = E(l, e);
                            m >= 0 && (Ea(l.length != o), Fa.splice.call(l, m, 1));
                            l.length == 0 && delete gc[f]
                        }
                        e.l = !0;
                        h.H = !0;
                        ic(g, j, f, h);
                        delete fc[d]
                    }
                }
            }
            return c
        }

        ac(function (a, b) {
            if (!fc[a])return!0;
            var c = fc[a], d = c.type, e = X;
            if (!(d in e))return!0;
            var e = e[d], f, g;
            Fb === n && (Fb = x && !r.addEventListener);
            if (Fb) {
                var h;
                if (!(h = b))a:{
                    h = "window.event".split(".");
                    for (var j = r; f = h.shift();)if (j[f] != o)j = j[f]; else {
                        h = o;
                        break a
                    }
                    h = j
                }
                f = h;
                h = !0 in e;
                j = !1 in e;
                if (h) {
                    if (f.keyCode < 0 || f.returnValue != n)return!0;
                    a:{
                        var l = !1;
                        if (f.keyCode == 0)try {
                            f.keyCode = -1;
                            break a
                        } catch (m) {
                            l = !0
                        }
                        if (l || f.returnValue == n)f.returnValue = !0
                    }
                }
                l = dc();
                l.q(f, this);
                f = !0;
                try {
                    if (h) {
                        for (var k = Zb(), p = l.currentTarget; p; p =
                                p.parentNode)k.push(p);
                        g = e[!0];
                        g.k = g.h;
                        for (var F = k.length - 1; !l.s && F >= 0 && g.k; F--)l.currentTarget = k[F], f &= kc(g, k[F], d, !0, l);
                        if (j) {
                            g = e[!1];
                            g.k = g.h;
                            for (F = 0; !l.s && F < k.length && g.k; F++)l.currentTarget = k[F], f &= kc(g, k[F], d, !1, l)
                        }
                    } else f = lc(c, l)
                } finally {
                    if (k)k.length = 0, $b(k);
                    l.n();
                    ec(l)
                }
                return f
            }
            d = new Rb(b, this);
            try {
                f = lc(c, d)
            } finally {
                d.n()
            }
            return f
        });
        var mc = {v: function (a) {
            return a.querySelectorAll && a.querySelector
        }};
        mc.b = function (a, b) {
            a || i(Error("No class name specified"));
            a = v(a);
            a.split(/\s+/).length > 1 && i(Error("Compound class names not permitted"));
            if (mc.v(b))return b.querySelector("." + a.replace(/\./g, "\\.")) || o;
            var c = J(H(b), "*", a, b);
            return c.length ? c[0] : o
        };
        mc.g = function (a, b) {
            a || i(Error("No class name specified"));
            a = v(a);
            a.split(/\s+/).length > 1 && i(Error("Compound class names not permitted"));
            if (mc.v(b))return b.querySelectorAll("." + a.replace(/\./g, "\\."));
            return J(H(b), "*", a, b)
        };
        var Y = {}, nc = {};
        Y.K = function (a, b, c) {
            b = J(H(b), "A", o, b);
            return Ia(b, function (b) {
                b = lb(b);
                return c && b.indexOf(a) != -1 || b == a
            })
        };
        Y.F = function (a, b, c) {
            b = J(H(b), "A", o, b);
            return G(b, function (b) {
                b = lb(b);
                return c && b.indexOf(a) != -1 || b == a
            })
        };
        Y.b = function (a, b) {
            return Y.K(a, b, !1)
        };
        Y.g = function (a, b) {
            return Y.F(a, b, !1)
        };
        nc.b = function (a, b) {
            return Y.K(a, b, !0)
        };
        nc.g = function (a, b) {
            return Y.F(a, b, !0)
        };
        var oc = {className: mc, css: {b: function (a, b) {
            !t(b.querySelector) && x && B(8) && !ca(b.querySelector) && i(Error("CSS selection is not supported"));
            a || i(Error("No selector specified"));
            a.split(/,/).length > 1 && i(Error("Compound selectors not permitted"));
            var a = v(a), c = b.querySelector(a);
            return c && c.nodeType == 1 ? c : o
        }, g: function (a, b) {
            !t(b.querySelectorAll) && x && B(8) && !ca(b.querySelector) && i(Error("CSS selection is not supported"));
            a || i(Error("No selector specified"));
            a.split(/,/).length > 1 && i(Error("Compound selectors not permitted"));
            a = v(a);
            return b.querySelectorAll(a)
        }}, id: {b: function (a, b) {
            var c = H(b), d = s(a) ? c.o.getElementById(a) : a;
            if (!d)return o;
            if (N(d, "id") == a && Oa(b, d))return d;
            c = J(c, "*");
            return Ia(c, function (c) {
                return N(c, "id") == a && Oa(b, c)
            })
        }, g: function (a, b) {
            var c = J(H(b), "*", o, b);
            return G(c, function (b) {
                return N(b, "id") == a
            })
        }}, linkText: Y, name: {b: function (a, b) {
            var c = J(H(b), "*", o, b);
            return Ia(c, function (b) {
                return N(b, "name") == a
            })
        }, g: function (a, b) {
            var c = J(H(b), "*", o, b);
            return G(c, function (b) {
                return N(b, "name") == a
            })
        }}, partialLinkText: nc,
            tagName: {b: function (a, b) {
                return b.getElementsByTagName(a)[0] || o
            }, g: function (a, b) {
                return b.getElementsByTagName(a)
            }}, xpath: Ta};

        function pc(a, b) {
            var c;
            a:{
                for (c in a)if (!Object.prototype[c])break a;
                c = o
            }
            if (c) {
                var d = oc[c];
                if (d && t(d.b))return d.b(a[c], b || C.document)
            }
            i(Error("Unsupported locator strategy: " + c))
        };
        var qc = {index: function (a, b) {
            a = Number(a);
            (isNaN(a) || a < 0) && i(new T("Illegal Index: " + a));
            b.length <= a && i(new T("Index out of range: " + a));
            return[b[a]]
        }, name: function (a, b) {
            return G(b, function (b) {
                return db(b, "name") == a
            })
        }, value: function (a, b) {
            return G(b, function (b) {
                return db(b, "value") === a
            })
        }};
        var Z = {};
        Z.T = function (a, b) {
            if (a.lastIndexOf("//", 0) == 0)return Z.M(a, b);
            if (a.lastIndexOf("document.", 0) == 0)return Z.A(a);
            return Z.C(a, b)
        };
        Z.N = function (a, b) {
            return rc(b || I(C), function (b) {
                return b.alt == a
            })
        };
        Z.O = function (a, b) {
            return rc(b || I(C), function (b) {
                return b.className == a
            })
        };
        Z.A = function (a) {
            var b = o;
            try {
                b = eval(a)
            } catch (c) {
                return o
            }
            return b ? b : o
        };
        Z.S = function (a, b) {
            return pc({id: a}, b)
        };
        Z.C = function (a, b) {
            return Z.id(a, b) || Z.name(a, b)
        };
        Z.U = function (a, b) {
            var c = b || I(C);
            H(c);
            var d = Na(document, "*", o, c), c = a.split(" ");
            for (c[0] = "name=" + c[0]; c.length;) {
                var e = c.shift(), f = "value", g = e.match(/^([A-Za-z]+)=(.+)/);
                g && (f = g[1].toLowerCase(), e = g[2]);
                (g = qc[f]) || i(new T("Unrecognised element-filter type: '" + f + "'"));
                d = g(e, d)
            }
            return d.length > 0 ? d[0] : o
        };
        Z.X = function (a, b) {
            try {
                var c;
                a:{
                    var d;
                    d = decodeURIComponent(a);
                    var e = b || document, f, g = e || document, h = g.$wdc_;
                    if (!h)h = g.$wdc_ = {}, h.I = ga();
                    if (!h.I)h.I = ga();
                    f = h;
                    d in f || i(new Jb(10, "Element does not exist in cache"));
                    var j = f[d];
                    if ("document"in j)j.closed && (delete f[d], i(new Jb(23, "Window has been closed."))), c = j; else {
                        for (g = j; g;) {
                            if (g == e.documentElement) {
                                c = j;
                                break a
                            }
                            g = g.parentNode
                        }
                        delete f[d];
                        i(new Jb(10, "Element is no longer attached to the DOM"))
                    }
                }
                return c
            } catch (l) {
                return o
            }
        };
        Z.M = function (a, b) {
            var c = ha(a, "/"), d = {xpath: a};
            try {
                var e = pc(d, b);
                if (e || !c)return e
            } catch (f) {
                c || i(f)
            }
            d = {xpath: a.substring(0, a.length - 1)};
            return pc(d, b)
        };
        Z.alt = Z.N;
        Z["class"] = Z.O;
        Z.dom = Z.A;
        Z.id = Z.S;
        Z.identifier = Z.C;
        Z.implicit = Z.T;
        Z.name = Z.U;
        Z.stored = Z.X;
        Z.xpath = Z.M;
        function sc(a, b, c, d) {
            var e = Z[a];
            e || i(new T("Unrecognised locator type: '" + a + "'"));
            c = e.call(o, b, c);
            if (c != o)return c;
            if (!d)return o;
            for (e = 0; e < d.frames.length; e++) {
                var f;
                try {
                    f = d.frames[e].document
                } catch (g) {
                }
                if (f && (c = sc(a, b, f, d.frames[e]), c != o))return c
            }
            return o
        }

        function rc(a, b) {
            for (var c = a.childNodes.length, d = 0; d < c; d++) {
                var e = a.childNodes[d];
                if (e.nodeType == 1) {
                    if (b(e))return e;
                    if (e = rc(e, b))return e
                }
            }
            return o
        };
        function tc(a, b, c) {
            if (s(a)) {
                var d;
                d = a;
                var e = d.match(/^([A-Za-z]+)=.+/);
                e ? (e = e[1].toLowerCase(), d = {type: e, string: d.substring(e.length + 1)}) : (e = {string: "", type: ""}, e.string = d, e.type = d.lastIndexOf("//", 0) == 0 ? "xpath" : d.lastIndexOf("document.", 0) == 0 ? "dom" : "identifier", d = e);
                e = C || C;
                d = sc(d.type, d.string, e.document, e);
                d = d != o ? d : o;
                d == o && i(new T("Element " + a + " not found"));
                a = d
            }
            c = c || "0,0";
            s(c) ? (c = c.split(/,/), c = {x: parseInt(c[0]), y: parseInt(c[1])}) : c = {x: 0, y: 0};
            c = (S[b] || Ib)(a, b, c);
            if (!("isTrusted"in c))c.aa = !1;
            x ?
                    a.fireEvent("on" + b, c) : a.dispatchEvent(c)
        }

        var uc = "_".split("."), $ = r;
        !(uc[0]in $) && $.execScript && $.execScript("var " + uc[0]);
        for (var vc; uc.length && (vc = uc.shift());)!uc.length && tc !== n ? $[vc] = tc : $ = $[vc] ? $[vc] : $[vc] = {};
        ;
        return this._.apply(null, arguments);
    }.apply({navigator: typeof window != 'undefined' ? window.navigator : null}, arguments);
}
