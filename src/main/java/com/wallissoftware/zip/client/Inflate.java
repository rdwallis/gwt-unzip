package com.wallissoftware.zip.client;

import com.wallissoftware.codec.client.Base64;

/*
 * This is taken from
 * https://github.com/dankogai/js-deflate/
 */

public class Inflate {
    static {
        initRawInflate();
        initRawDeflate();
    }


    public static final String deflate(final String data) {
        return deflateRaw(Base64.INSTANCE.encode(data));
    }

    public static final native String deflateRaw(final String data) /*-{
		return $wnd.RawDeflate.deflate(data);
    }-*/;

    public static final String inflate(final String data) {
        return Base64.INSTANCE.decode(inflateRaw(data));
    }

    public static final native String inflateRaw(final String data) /*-{
		return $wnd.RawDeflate.inflate(data);
    }-*/;

    private static native void initRawDeflate()/*-{
		(function(ctx) {
		var zip_WSIZE = 32768;
		var zip_STORED_BLOCK = 0;
		var zip_STATIC_TREES = 1;
		var zip_DYN_TREES = 2;
		var zip_DEFAULT_LEVEL = 6;
		var zip_FULL_SEARCH = true;
		var zip_INBUFSIZ = 32768;
		var zip_INBUF_EXTRA = 64;
		var zip_OUTBUFSIZ = 1024 * 8;
		var zip_window_size = 2 * zip_WSIZE;
		var zip_MIN_MATCH = 3;
		var zip_MAX_MATCH = 258;
		var zip_BITS = 16;
		var zip_LIT_BUFSIZE = 8192;
		var zip_HASH_BITS = 13;
		if (zip_LIT_BUFSIZE > zip_INBUFSIZ) {
		alert("error: zip_INBUFSIZ is too small");
		}
		if (zip_WSIZE << 1 > 1 << zip_BITS) {
		alert("error: zip_WSIZE is too large");
		}
		if (zip_HASH_BITS > zip_BITS - 1) {
		alert("error: zip_HASH_BITS is too large");
		}
		if (zip_HASH_BITS < 8 || zip_MAX_MATCH != 258) {
		alert("error: Code too clever");
		}
		var zip_DIST_BUFSIZE = zip_LIT_BUFSIZE;
		var zip_HASH_SIZE = 1 << zip_HASH_BITS;
		var zip_HASH_MASK = zip_HASH_SIZE - 1;
		var zip_WMASK = zip_WSIZE - 1;
		var zip_NIL = 0;
		var zip_TOO_FAR = 4096;
		var zip_MIN_LOOKAHEAD = zip_MAX_MATCH + zip_MIN_MATCH + 1;
		var zip_MAX_DIST = zip_WSIZE - zip_MIN_LOOKAHEAD;
		var zip_SMALLEST = 1;
		var zip_MAX_BITS = 15;
		var zip_MAX_BL_BITS = 7;
		var zip_LENGTH_CODES = 29;
		var zip_LITERALS = 256;
		var zip_END_BLOCK = 256;
		var zip_L_CODES = zip_LITERALS + 1 + zip_LENGTH_CODES;
		var zip_D_CODES = 30;
		var zip_BL_CODES = 19;
		var zip_REP_3_6 = 16;
		var zip_REPZ_3_10 = 17;
		var zip_REPZ_11_138 = 18;
		var zip_HEAP_SIZE = 2 * zip_L_CODES + 1;
		var zip_H_SHIFT = parseInt((zip_HASH_BITS + zip_MIN_MATCH - 1) / zip_MIN_MATCH);
		var zip_free_queue;
		var zip_qhead, zip_qtail;
		var zip_initflag;
		var zip_outbuf = null;
		var zip_outcnt, zip_outoff;
		var zip_complete;
		var zip_window;
		var zip_d_buf;
		var zip_l_buf;
		var zip_prev;
		var zip_bi_buf;
		var zip_bi_valid;
		var zip_block_start;
		var zip_ins_h;
		var zip_hash_head;
		var zip_prev_match;
		var zip_match_available;
		var zip_match_length;
		var zip_prev_length;
		var zip_strstart;
		var zip_match_start;
		var zip_eofile;
		var zip_lookahead;
		var zip_max_chain_length;
		var zip_max_lazy_match;
		var zip_compr_level;
		var zip_good_match;
		var zip_nice_match;
		var zip_dyn_ltree;
		var zip_dyn_dtree;
		var zip_static_ltree;
		var zip_static_dtree;
		var zip_bl_tree;
		var zip_l_desc;
		var zip_d_desc;
		var zip_bl_desc;
		var zip_bl_count;
		var zip_heap;
		var zip_heap_len;
		var zip_heap_max;
		var zip_depth;
		var zip_length_code;
		var zip_dist_code;
		var zip_base_length;
		var zip_base_dist;
		var zip_flag_buf;
		var zip_last_lit;
		var zip_last_dist;
		var zip_last_flags;
		var zip_flags;
		var zip_flag_bit;
		var zip_opt_len;
		var zip_static_len;
		var zip_deflate_data;
		var zip_deflate_pos;
		var zip_DeflateCT = function() {
		this.fc = 0;
		this.dl = 0;
		};
		var zip_DeflateTreeDesc = function() {
		this.dyn_tree = null;
		this.static_tree = null;
		this.extra_bits = null;
		this.extra_base = 0;
		this.elems = 0;
		this.max_length = 0;
		this.max_code = 0;
		};
		var zip_DeflateConfiguration = function(a, b, c, d) {
		this.good_length = a;
		this.max_lazy = b;
		this.nice_length = c;
		this.max_chain = d;
		};
		var zip_DeflateBuffer = function() {
		this.next = null;
		this.len = 0;
		this.ptr = new Array(zip_OUTBUFSIZ);
		this.off = 0;
		};
		var zip_extra_lbits = new Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0);
		var zip_extra_dbits = new Array(0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13);
		var zip_extra_blbits = new Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 7);
		var zip_bl_order = new Array(16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15);
		var zip_configuration_table = new Array(new zip_DeflateConfiguration(0, 0, 0, 0), new zip_DeflateConfiguration(4, 4, 8, 4), new zip_DeflateConfiguration(4, 5, 16, 8), new zip_DeflateConfiguration(4, 6, 32, 32), new zip_DeflateConfiguration(4, 4, 16, 16), new zip_DeflateConfiguration(8, 16,
				32, 32), new zip_DeflateConfiguration(8, 16, 128, 128), new zip_DeflateConfiguration(8, 32, 128, 256), new zip_DeflateConfiguration(32, 128, 258, 1024), new zip_DeflateConfiguration(32, 258, 258, 4096));
		var zip_deflate_start = function(level) {
		var i;
		if (!level) {
		level = zip_DEFAULT_LEVEL;
		} else {
		if (level < 1) {
		level = 1;
		} else {
		if (level > 9) {
		level = 9;
		}
		}
		}
		zip_compr_level = level;
		zip_initflag = false;
		zip_eofile = false;
		if (zip_outbuf != null) {
		return;
		}
		zip_free_queue = zip_qhead = zip_qtail = null;
		zip_outbuf = new Array(zip_OUTBUFSIZ);
		zip_window = new Array(zip_window_size);
		zip_d_buf = new Array(zip_DIST_BUFSIZE);
		zip_l_buf = new Array(zip_INBUFSIZ + zip_INBUF_EXTRA);
		zip_prev = new Array(1 << zip_BITS);
		zip_dyn_ltree = new Array(zip_HEAP_SIZE);
		for (i = 0; i < zip_HEAP_SIZE; i++) {
		zip_dyn_ltree[i] = new zip_DeflateCT;
		}
		zip_dyn_dtree = new Array(2 * zip_D_CODES + 1);
		for (i = 0; i < 2 * zip_D_CODES + 1; i++) {
		zip_dyn_dtree[i] = new zip_DeflateCT;
		}
		zip_static_ltree = new Array(zip_L_CODES + 2);
		for (i = 0; i < zip_L_CODES + 2; i++) {
		zip_static_ltree[i] = new zip_DeflateCT;
		}
		zip_static_dtree = new Array(zip_D_CODES);
		for (i = 0; i < zip_D_CODES; i++) {
		zip_static_dtree[i] = new zip_DeflateCT;
		}
		zip_bl_tree = new Array(2 * zip_BL_CODES + 1);
		for (i = 0; i < 2 * zip_BL_CODES + 1; i++) {
		zip_bl_tree[i] = new zip_DeflateCT;
		}
		zip_l_desc = new zip_DeflateTreeDesc;
		zip_d_desc = new zip_DeflateTreeDesc;
		zip_bl_desc = new zip_DeflateTreeDesc;
		zip_bl_count = new Array(zip_MAX_BITS + 1);
		zip_heap = new Array(2 * zip_L_CODES + 1);
		zip_depth = new Array(2 * zip_L_CODES + 1);
		zip_length_code = new Array(zip_MAX_MATCH - zip_MIN_MATCH + 1);
		zip_dist_code = new Array(512);
		zip_base_length = new Array(zip_LENGTH_CODES);
		zip_base_dist = new Array(zip_D_CODES);
		zip_flag_buf = new Array(parseInt(zip_LIT_BUFSIZE / 8));
		};
		var zip_deflate_end = function() {
		zip_free_queue = zip_qhead = zip_qtail = null;
		zip_outbuf = null;
		zip_window = null;
		zip_d_buf = null;
		zip_l_buf = null;
		zip_prev = null;
		zip_dyn_ltree = null;
		zip_dyn_dtree = null;
		zip_static_ltree = null;
		zip_static_dtree = null;
		zip_bl_tree = null;
		zip_l_desc = null;
		zip_d_desc = null;
		zip_bl_desc = null;
		zip_bl_count = null;
		zip_heap = null;
		zip_depth = null;
		zip_length_code = null;
		zip_dist_code = null;
		zip_base_length = null;
		zip_base_dist = null;
		zip_flag_buf = null;
		};
		var zip_reuse_queue = function(p) {
		p.next = zip_free_queue;
		zip_free_queue = p;
		};
		var zip_new_queue = function() {
		var p;
		if (zip_free_queue != null) {
		p = zip_free_queue;
		zip_free_queue = zip_free_queue.next;
		} else {
		p = new zip_DeflateBuffer;
		}
		p.next = null;
		p.len = p.off = 0;
		return p;
		};
		var zip_head1 = function(i) {
		return zip_prev[zip_WSIZE + i];
		};
		var zip_head2 = function(i, val) {
		return zip_prev[zip_WSIZE + i] = val;
		};
		var zip_put_byte = function(c) {
		zip_outbuf[zip_outoff + zip_outcnt++] = c;
		if (zip_outoff + zip_outcnt == zip_OUTBUFSIZ) {
		zip_qoutbuf();
		}
		};
		var zip_put_short = function(w) {
		w &= 65535;
		if (zip_outoff + zip_outcnt < zip_OUTBUFSIZ - 2) {
		zip_outbuf[zip_outoff + zip_outcnt++] = w & 255;
		zip_outbuf[zip_outoff + zip_outcnt++] = w >>> 8;
		} else {
		zip_put_byte(w & 255);
		zip_put_byte(w >>> 8);
		}
		};
		var zip_INSERT_STRING = function() {
		zip_ins_h = (zip_ins_h << zip_H_SHIFT ^ zip_window[zip_strstart + zip_MIN_MATCH - 1] & 255) & zip_HASH_MASK;
		zip_hash_head = zip_head1(zip_ins_h);
		zip_prev[zip_strstart & zip_WMASK] = zip_hash_head;
		zip_head2(zip_ins_h, zip_strstart);
		};
		var zip_SEND_CODE = function(c, tree) {
		zip_send_bits(tree[c].fc, tree[c].dl);
		};
		var zip_D_CODE = function(dist) {
		return (dist < 256 ? zip_dist_code[dist] : zip_dist_code[256 + (dist >> 7)]) & 255;
		};
		var zip_SMALLER = function(tree, n, m) {
		return tree[n].fc < tree[m].fc || tree[n].fc == tree[m].fc && zip_depth[n] <= zip_depth[m];
		};
		var zip_read_buff = function(buff, offset, n) {
		var i;
		for (i = 0; i < n && zip_deflate_pos < zip_deflate_data.length; i++) {
		buff[offset + i] = zip_deflate_data.charCodeAt(zip_deflate_pos++) & 255;
		}
		return i;
		};
		var zip_lm_init = function() {
		var j;
		for (j = 0; j < zip_HASH_SIZE; j++) {
		zip_prev[zip_WSIZE + j] = 0;
		}
		zip_max_lazy_match = zip_configuration_table[zip_compr_level].max_lazy;
		zip_good_match = zip_configuration_table[zip_compr_level].good_length;
		if (!zip_FULL_SEARCH) {
		zip_nice_match = zip_configuration_table[zip_compr_level].nice_length;
		}
		zip_max_chain_length = zip_configuration_table[zip_compr_level].max_chain;
		zip_strstart = 0;
		zip_block_start = 0;
		zip_lookahead = zip_read_buff(zip_window, 0, 2 * zip_WSIZE);
		if (zip_lookahead <= 0) {
		zip_eofile = true;
		zip_lookahead = 0;
		return;
		}
		zip_eofile = false;
		while (zip_lookahead < zip_MIN_LOOKAHEAD && !zip_eofile) {
		zip_fill_window();
		}
		zip_ins_h = 0;
		for (j = 0; j < zip_MIN_MATCH - 1; j++) {
		zip_ins_h = (zip_ins_h << zip_H_SHIFT ^ zip_window[j] & 255) & zip_HASH_MASK;
		}
		};
		var zip_longest_match = function(cur_match) {
		var chain_length = zip_max_chain_length;
		var scanp = zip_strstart;
		var matchp;
		var len;
		var best_len = zip_prev_length;
		var limit = zip_strstart > zip_MAX_DIST ? zip_strstart - zip_MAX_DIST : zip_NIL;
		var strendp = zip_strstart + zip_MAX_MATCH;
		var scan_end1 = zip_window[scanp + best_len - 1];
		var scan_end = zip_window[scanp + best_len];
		if (zip_prev_length >= zip_good_match) {
		chain_length >>= 2;
		}
		do {
		matchp = cur_match;
		if (zip_window[matchp + best_len] != scan_end || zip_window[matchp + best_len - 1] != scan_end1 || zip_window[matchp] != zip_window[scanp] || zip_window[++matchp] != zip_window[scanp + 1]) {
		continue;
		}
		scanp += 2;
		matchp++;
		do {
		} while (zip_window[++scanp] == zip_window[++matchp] && zip_window[++scanp] == zip_window[++matchp] && zip_window[++scanp] == zip_window[++matchp] && zip_window[++scanp] == zip_window[++matchp] && zip_window[++scanp] == zip_window[++matchp] && zip_window[++scanp] == zip_window[++matchp]
				&& zip_window[++scanp] == zip_window[++matchp] && zip_window[++scanp] == zip_window[++matchp] && scanp < strendp);
		len = zip_MAX_MATCH - (strendp - scanp);
		scanp = strendp - zip_MAX_MATCH;
		if (len > best_len) {
		zip_match_start = cur_match;
		best_len = len;
		if (zip_FULL_SEARCH) {
		if (len >= zip_MAX_MATCH) {
		break;
		}
		} else {
		if (len >= zip_nice_match) {
		break;
		}
		}
		scan_end1 = zip_window[scanp + best_len - 1];
		scan_end = zip_window[scanp + best_len];
		}
		} while ((cur_match = zip_prev[cur_match & zip_WMASK]) > limit && --chain_length != 0);
		return best_len;
		};
		var zip_fill_window = function() {
		var n, m;
		var more = zip_window_size - zip_lookahead - zip_strstart;
		if (more == -1) {
		more--;
		} else {
		if (zip_strstart >= zip_WSIZE + zip_MAX_DIST) {
		for (n = 0; n < zip_WSIZE; n++) {
		zip_window[n] = zip_window[n + zip_WSIZE];
		}
		zip_match_start -= zip_WSIZE;
		zip_strstart -= zip_WSIZE;
		zip_block_start -= zip_WSIZE;
		for (n = 0; n < zip_HASH_SIZE; n++) {
		m = zip_head1(n);
		zip_head2(n, m >= zip_WSIZE ? m - zip_WSIZE : zip_NIL);
		}
		for (n = 0; n < zip_WSIZE; n++) {
		m = zip_prev[n];
		zip_prev[n] = m >= zip_WSIZE ? m - zip_WSIZE : zip_NIL;
		}
		more += zip_WSIZE;
		}
		}
		if (!zip_eofile) {
		n = zip_read_buff(zip_window, zip_strstart + zip_lookahead, more);
		if (n <= 0) {
		zip_eofile = true;
		} else {
		zip_lookahead += n;
		}
		}
		};
		var zip_deflate_fast = function() {
		while (zip_lookahead != 0 && zip_qhead == null) {
		var flush;
		zip_INSERT_STRING();
		if (zip_hash_head != zip_NIL && zip_strstart - zip_hash_head <= zip_MAX_DIST) {
		zip_match_length = zip_longest_match(zip_hash_head);
		if (zip_match_length > zip_lookahead) {
		zip_match_length = zip_lookahead;
		}
		}
		if (zip_match_length >= zip_MIN_MATCH) {
		flush = zip_ct_tally(zip_strstart - zip_match_start, zip_match_length - zip_MIN_MATCH);
		zip_lookahead -= zip_match_length;
		if (zip_match_length <= zip_max_lazy_match) {
		zip_match_length--;
		do {
		zip_strstart++;
		zip_INSERT_STRING();
		} while (--zip_match_length != 0);
		zip_strstart++;
		} else {
		zip_strstart += zip_match_length;
		zip_match_length = 0;
		zip_ins_h = zip_window[zip_strstart] & 255;
		zip_ins_h = (zip_ins_h << zip_H_SHIFT ^ zip_window[zip_strstart + 1] & 255) & zip_HASH_MASK;
		}
		} else {
		flush = zip_ct_tally(0, zip_window[zip_strstart] & 255);
		zip_lookahead--;
		zip_strstart++;
		}
		if (flush) {
		zip_flush_block(0);
		zip_block_start = zip_strstart;
		}
		while (zip_lookahead < zip_MIN_LOOKAHEAD && !zip_eofile) {
		zip_fill_window();
		}
		}
		};
		var zip_deflate_better = function() {
		while (zip_lookahead != 0 && zip_qhead == null) {
		zip_INSERT_STRING();
		zip_prev_length = zip_match_length;
		zip_prev_match = zip_match_start;
		zip_match_length = zip_MIN_MATCH - 1;
		if (zip_hash_head != zip_NIL && zip_prev_length < zip_max_lazy_match && zip_strstart - zip_hash_head <= zip_MAX_DIST) {
		zip_match_length = zip_longest_match(zip_hash_head);
		if (zip_match_length > zip_lookahead) {
		zip_match_length = zip_lookahead;
		}
		if (zip_match_length == zip_MIN_MATCH && zip_strstart - zip_match_start > zip_TOO_FAR) {
		zip_match_length--;
		}
		}
		if (zip_prev_length >= zip_MIN_MATCH && zip_match_length <= zip_prev_length) {
		var flush;
		flush = zip_ct_tally(zip_strstart - 1 - zip_prev_match, zip_prev_length - zip_MIN_MATCH);
		zip_lookahead -= zip_prev_length - 1;
		zip_prev_length -= 2;
		do {
		zip_strstart++;
		zip_INSERT_STRING();
		} while (--zip_prev_length != 0);
		zip_match_available = 0;
		zip_match_length = zip_MIN_MATCH - 1;
		zip_strstart++;
		if (flush) {
		zip_flush_block(0);
		zip_block_start = zip_strstart;
		}
		} else {
		if (zip_match_available != 0) {
		if (zip_ct_tally(0, zip_window[zip_strstart - 1] & 255)) {
		zip_flush_block(0);
		zip_block_start = zip_strstart;
		}
		zip_strstart++;
		zip_lookahead--;
		} else {
		zip_match_available = 1;
		zip_strstart++;
		zip_lookahead--;
		}
		}
		while (zip_lookahead < zip_MIN_LOOKAHEAD && !zip_eofile) {
		zip_fill_window();
		}
		}
		};
		var zip_init_deflate = function() {
		if (zip_eofile) {
		return;
		}
		zip_bi_buf = 0;
		zip_bi_valid = 0;
		zip_ct_init();
		zip_lm_init();
		zip_qhead = null;
		zip_outcnt = 0;
		zip_outoff = 0;
		zip_match_available = 0;
		if (zip_compr_level <= 3) {
		zip_prev_length = zip_MIN_MATCH - 1;
		zip_match_length = 0;
		} else {
		zip_match_length = zip_MIN_MATCH - 1;
		zip_match_available = 0;
		zip_match_available = 0;
		}
		zip_complete = false;
		};
		var zip_deflate_internal = function(buff, off, buff_size) {
		var n;
		if (!zip_initflag) {
		zip_init_deflate();
		zip_initflag = true;
		if (zip_lookahead == 0) {
		zip_complete = true;
		return 0;
		}
		}
		if ((n = zip_qcopy(buff, off, buff_size)) == buff_size) {
		return buff_size;
		}
		if (zip_complete) {
		return n;
		}
		if (zip_compr_level <= 3) {
		zip_deflate_fast();
		} else {
		zip_deflate_better();
		}
		if (zip_lookahead == 0) {
		if (zip_match_available != 0) {
		zip_ct_tally(0, zip_window[zip_strstart - 1] & 255);
		}
		zip_flush_block(1);
		zip_complete = true;
		}
		return n + zip_qcopy(buff, n + off, buff_size - n);
		};
		var zip_qcopy = function(buff, off, buff_size) {
		var n, i, j;
		n = 0;
		while (zip_qhead != null && n < buff_size) {
		i = buff_size - n;
		if (i > zip_qhead.len) {
		i = zip_qhead.len;
		}
		for (j = 0; j < i; j++) {
		buff[off + n + j] = zip_qhead.ptr[zip_qhead.off + j];
		}
		zip_qhead.off += i;
		zip_qhead.len -= i;
		n += i;
		if (zip_qhead.len == 0) {
		var p;
		p = zip_qhead;
		zip_qhead = zip_qhead.next;
		zip_reuse_queue(p);
		}
		}
		if (n == buff_size) {
		return n;
		}
		if (zip_outoff < zip_outcnt) {
		i = buff_size - n;
		if (i > zip_outcnt - zip_outoff) {
		i = zip_outcnt - zip_outoff;
		}
		for (j = 0; j < i; j++) {
		buff[off + n + j] = zip_outbuf[zip_outoff + j];
		}
		zip_outoff += i;
		n += i;
		if (zip_outcnt == zip_outoff) {
		zip_outcnt = zip_outoff = 0;
		}
		}
		return n;
		};
		var zip_ct_init = function() {
		var n;
		var bits;
		var length;
		var code;
		var dist;
		if (zip_static_dtree[0].dl != 0) {
		return;
		}
		zip_l_desc.dyn_tree = zip_dyn_ltree;
		zip_l_desc.static_tree = zip_static_ltree;
		zip_l_desc.extra_bits = zip_extra_lbits;
		zip_l_desc.extra_base = zip_LITERALS + 1;
		zip_l_desc.elems = zip_L_CODES;
		zip_l_desc.max_length = zip_MAX_BITS;
		zip_l_desc.max_code = 0;
		zip_d_desc.dyn_tree = zip_dyn_dtree;
		zip_d_desc.static_tree = zip_static_dtree;
		zip_d_desc.extra_bits = zip_extra_dbits;
		zip_d_desc.extra_base = 0;
		zip_d_desc.elems = zip_D_CODES;
		zip_d_desc.max_length = zip_MAX_BITS;
		zip_d_desc.max_code = 0;
		zip_bl_desc.dyn_tree = zip_bl_tree;
		zip_bl_desc.static_tree = null;
		zip_bl_desc.extra_bits = zip_extra_blbits;
		zip_bl_desc.extra_base = 0;
		zip_bl_desc.elems = zip_BL_CODES;
		zip_bl_desc.max_length = zip_MAX_BL_BITS;
		zip_bl_desc.max_code = 0;
		length = 0;
		for (code = 0; code < zip_LENGTH_CODES - 1; code++) {
		zip_base_length[code] = length;
		for (n = 0; n < 1 << zip_extra_lbits[code]; n++) {
		zip_length_code[length++] = code;
		}
		}
		zip_length_code[length - 1] = code;
		dist = 0;
		for (code = 0; code < 16; code++) {
		zip_base_dist[code] = dist;
		for (n = 0; n < 1 << zip_extra_dbits[code]; n++) {
		zip_dist_code[dist++] = code;
		}
		}
		dist >>= 7;
		for (; code < zip_D_CODES; code++) {
		zip_base_dist[code] = dist << 7;
		for (n = 0; n < 1 << zip_extra_dbits[code] - 7; n++) {
		zip_dist_code[256 + dist++] = code;
		}
		}
		for (bits = 0; bits <= zip_MAX_BITS; bits++) {
		zip_bl_count[bits] = 0;
		}
		n = 0;
		while (n <= 143) {
		zip_static_ltree[n++].dl = 8;
		zip_bl_count[8]++;
		}
		while (n <= 255) {
		zip_static_ltree[n++].dl = 9;
		zip_bl_count[9]++;
		}
		while (n <= 279) {
		zip_static_ltree[n++].dl = 7;
		zip_bl_count[7]++;
		}
		while (n <= 287) {
		zip_static_ltree[n++].dl = 8;
		zip_bl_count[8]++;
		}
		zip_gen_codes(zip_static_ltree, zip_L_CODES + 1);
		for (n = 0; n < zip_D_CODES; n++) {
		zip_static_dtree[n].dl = 5;
		zip_static_dtree[n].fc = zip_bi_reverse(n, 5);
		}
		zip_init_block();
		};
		var zip_init_block = function() {
		var n;
		for (n = 0; n < zip_L_CODES; n++) {
		zip_dyn_ltree[n].fc = 0;
		}
		for (n = 0; n < zip_D_CODES; n++) {
		zip_dyn_dtree[n].fc = 0;
		}
		for (n = 0; n < zip_BL_CODES; n++) {
		zip_bl_tree[n].fc = 0;
		}
		zip_dyn_ltree[zip_END_BLOCK].fc = 1;
		zip_opt_len = zip_static_len = 0;
		zip_last_lit = zip_last_dist = zip_last_flags = 0;
		zip_flags = 0;
		zip_flag_bit = 1;
		};
		var zip_pqdownheap = function(tree, k) {
		var v = zip_heap[k];
		var j = k << 1;
		while (j <= zip_heap_len) {
		if (j < zip_heap_len && zip_SMALLER(tree, zip_heap[j + 1], zip_heap[j])) {
		j++;
		}
		if (zip_SMALLER(tree, v, zip_heap[j])) {
		break;
		}
		zip_heap[k] = zip_heap[j];
		k = j;
		j <<= 1;
		}
		zip_heap[k] = v;
		};
		var zip_gen_bitlen = function(desc) {
		var tree = desc.dyn_tree;
		var extra = desc.extra_bits;
		var base = desc.extra_base;
		var max_code = desc.max_code;
		var max_length = desc.max_length;
		var stree = desc.static_tree;
		var h;
		var n, m;
		var bits;
		var xbits;
		var f;
		var overflow = 0;
		for (bits = 0; bits <= zip_MAX_BITS; bits++) {
		zip_bl_count[bits] = 0;
		}
		tree[zip_heap[zip_heap_max]].dl = 0;
		for (h = zip_heap_max + 1; h < zip_HEAP_SIZE; h++) {
		n = zip_heap[h];
		bits = tree[tree[n].dl].dl + 1;
		if (bits > max_length) {
		bits = max_length;
		overflow++;
		}
		tree[n].dl = bits;
		if (n > max_code) {
		continue;
		}
		zip_bl_count[bits]++;
		xbits = 0;
		if (n >= base) {
		xbits = extra[n - base];
		}
		f = tree[n].fc;
		zip_opt_len += f * (bits + xbits);
		if (stree != null) {
		zip_static_len += f * (stree[n].dl + xbits);
		}
		}
		if (overflow == 0) {
		return;
		}
		do {
		bits = max_length - 1;
		while (zip_bl_count[bits] == 0) {
		bits--;
		}
		zip_bl_count[bits]--;
		zip_bl_count[bits + 1] += 2;
		zip_bl_count[max_length]--;
		overflow -= 2;
		} while (overflow > 0);
		for (bits = max_length; bits != 0; bits--) {
		n = zip_bl_count[bits];
		while (n != 0) {
		m = zip_heap[--h];
		if (m > max_code) {
		continue;
		}
		if (tree[m].dl != bits) {
		zip_opt_len += (bits - tree[m].dl) * tree[m].fc;
		tree[m].fc = bits;
		}
		n--;
		}
		}
		};
		var zip_gen_codes = function(tree, max_code) {
		var next_code = new Array(zip_MAX_BITS + 1);
		var code = 0;
		var bits;
		var n;
		for (bits = 1; bits <= zip_MAX_BITS; bits++) {
		code = code + zip_bl_count[bits - 1] << 1;
		next_code[bits] = code;
		}
		for (n = 0; n <= max_code; n++) {
		var len = tree[n].dl;
		if (len == 0) {
		continue;
		}
		tree[n].fc = zip_bi_reverse(next_code[len]++, len);
		}
		};
		var zip_build_tree = function(desc) {
		var tree = desc.dyn_tree;
		var stree = desc.static_tree;
		var elems = desc.elems;
		var n, m;
		var max_code = -1;
		var node = elems;
		zip_heap_len = 0;
		zip_heap_max = zip_HEAP_SIZE;
		for (n = 0; n < elems; n++) {
		if (tree[n].fc != 0) {
		zip_heap[++zip_heap_len] = max_code = n;
		zip_depth[n] = 0;
		} else {
		tree[n].dl = 0;
		}
		}
		while (zip_heap_len < 2) {
		var xnew = zip_heap[++zip_heap_len] = max_code < 2 ? ++max_code : 0;
		tree[xnew].fc = 1;
		zip_depth[xnew] = 0;
		zip_opt_len--;
		if (stree != null) {
		zip_static_len -= stree[xnew].dl;
		}
		}
		desc.max_code = max_code;
		for (n = zip_heap_len >> 1; n >= 1; n--) {
		zip_pqdownheap(tree, n);
		}
		do {
		n = zip_heap[zip_SMALLEST];
		zip_heap[zip_SMALLEST] = zip_heap[zip_heap_len--];
		zip_pqdownheap(tree, zip_SMALLEST);
		m = zip_heap[zip_SMALLEST];
		zip_heap[--zip_heap_max] = n;
		zip_heap[--zip_heap_max] = m;
		tree[node].fc = tree[n].fc + tree[m].fc;
		if (zip_depth[n] > zip_depth[m] + 1) {
		zip_depth[node] = zip_depth[n];
		} else {
		zip_depth[node] = zip_depth[m] + 1;
		}
		tree[n].dl = tree[m].dl = node;
		zip_heap[zip_SMALLEST] = node++;
		zip_pqdownheap(tree, zip_SMALLEST);
		} while (zip_heap_len >= 2);
		zip_heap[--zip_heap_max] = zip_heap[zip_SMALLEST];
		zip_gen_bitlen(desc);
		zip_gen_codes(tree, max_code);
		};
		var zip_scan_tree = function(tree, max_code) {
		var n;
		var prevlen = -1;
		var curlen;
		var nextlen = tree[0].dl;
		var count = 0;
		var max_count = 7;
		var min_count = 4;
		if (nextlen == 0) {
		max_count = 138;
		min_count = 3;
		}
		tree[max_code + 1].dl = 65535;
		for (n = 0; n <= max_code; n++) {
		curlen = nextlen;
		nextlen = tree[n + 1].dl;
		if (++count < max_count && curlen == nextlen) {
		continue;
		} else {
		if (count < min_count) {
		zip_bl_tree[curlen].fc += count;
		} else {
		if (curlen != 0) {
		if (curlen != prevlen) {
		zip_bl_tree[curlen].fc++;
		}
		zip_bl_tree[zip_REP_3_6].fc++;
		} else {
		if (count <= 10) {
		zip_bl_tree[zip_REPZ_3_10].fc++;
		} else {
		zip_bl_tree[zip_REPZ_11_138].fc++;
		}
		}
		}
		}
		count = 0;
		prevlen = curlen;
		if (nextlen == 0) {
		max_count = 138;
		min_count = 3;
		} else {
		if (curlen == nextlen) {
		max_count = 6;
		min_count = 3;
		} else {
		max_count = 7;
		min_count = 4;
		}
		}
		}
		};
		var zip_send_tree = function(tree, max_code) {
		var n;
		var prevlen = -1;
		var curlen;
		var nextlen = tree[0].dl;
		var count = 0;
		var max_count = 7;
		var min_count = 4;
		if (nextlen == 0) {
		max_count = 138;
		min_count = 3;
		}
		for (n = 0; n <= max_code; n++) {
		curlen = nextlen;
		nextlen = tree[n + 1].dl;
		if (++count < max_count && curlen == nextlen) {
		continue;
		} else {
		if (count < min_count) {
		do {
		zip_SEND_CODE(curlen, zip_bl_tree);
		} while (--count != 0);
		} else {
		if (curlen != 0) {
		if (curlen != prevlen) {
		zip_SEND_CODE(curlen, zip_bl_tree);
		count--;
		}
		zip_SEND_CODE(zip_REP_3_6, zip_bl_tree);
		zip_send_bits(count - 3, 2);
		} else {
		if (count <= 10) {
		zip_SEND_CODE(zip_REPZ_3_10, zip_bl_tree);
		zip_send_bits(count - 3, 3);
		} else {
		zip_SEND_CODE(zip_REPZ_11_138, zip_bl_tree);
		zip_send_bits(count - 11, 7);
		}
		}
		}
		}
		count = 0;
		prevlen = curlen;
		if (nextlen == 0) {
		max_count = 138;
		min_count = 3;
		} else {
		if (curlen == nextlen) {
		max_count = 6;
		min_count = 3;
		} else {
		max_count = 7;
		min_count = 4;
		}
		}
		}
		};
		var zip_build_bl_tree = function() {
		var max_blindex;
		zip_scan_tree(zip_dyn_ltree, zip_l_desc.max_code);
		zip_scan_tree(zip_dyn_dtree, zip_d_desc.max_code);
		zip_build_tree(zip_bl_desc);
		for (max_blindex = zip_BL_CODES - 1; max_blindex >= 3; max_blindex--) {
		if (zip_bl_tree[zip_bl_order[max_blindex]].dl != 0) {
		break;
		}
		}
		zip_opt_len += 3 * (max_blindex + 1) + 5 + 5 + 4;
		return max_blindex;
		};
		var zip_send_all_trees = function(lcodes, dcodes, blcodes) {
		var rank;
		zip_send_bits(lcodes - 257, 5);
		zip_send_bits(dcodes - 1, 5);
		zip_send_bits(blcodes - 4, 4);
		for (rank = 0; rank < blcodes; rank++) {
		zip_send_bits(zip_bl_tree[zip_bl_order[rank]].dl, 3);
		}
		zip_send_tree(zip_dyn_ltree, lcodes - 1);
		zip_send_tree(zip_dyn_dtree, dcodes - 1);
		};
		var zip_flush_block = function(eof) {
		var opt_lenb, static_lenb;
		var max_blindex;
		var stored_len;
		stored_len = zip_strstart - zip_block_start;
		zip_flag_buf[zip_last_flags] = zip_flags;
		zip_build_tree(zip_l_desc);
		zip_build_tree(zip_d_desc);
		max_blindex = zip_build_bl_tree();
		opt_lenb = zip_opt_len + 3 + 7 >> 3;
		static_lenb = zip_static_len + 3 + 7 >> 3;
		if (static_lenb <= opt_lenb) {
		opt_lenb = static_lenb;
		}
		if (stored_len + 4 <= opt_lenb && zip_block_start >= 0) {
		var i;
		zip_send_bits((zip_STORED_BLOCK << 1) + eof, 3);
		zip_bi_windup();
		zip_put_short(stored_len);
		zip_put_short(~stored_len);
		for (i = 0; i < stored_len; i++) {
		zip_put_byte(zip_window[zip_block_start + i]);
		}
		} else {
		if (static_lenb == opt_lenb) {
		zip_send_bits((zip_STATIC_TREES << 1) + eof, 3);
		zip_compress_block(zip_static_ltree, zip_static_dtree);
		} else {
		zip_send_bits((zip_DYN_TREES << 1) + eof, 3);
		zip_send_all_trees(zip_l_desc.max_code + 1, zip_d_desc.max_code + 1, max_blindex + 1);
		zip_compress_block(zip_dyn_ltree, zip_dyn_dtree);
		}
		}
		zip_init_block();
		if (eof != 0) {
		zip_bi_windup();
		}
		};
		var zip_ct_tally = function(dist, lc) {
		zip_l_buf[zip_last_lit++] = lc;
		if (dist == 0) {
		zip_dyn_ltree[lc].fc++;
		} else {
		dist--;
		zip_dyn_ltree[zip_length_code[lc] + zip_LITERALS + 1].fc++;
		zip_dyn_dtree[zip_D_CODE(dist)].fc++;
		zip_d_buf[zip_last_dist++] = dist;
		zip_flags |= zip_flag_bit;
		}
		zip_flag_bit <<= 1;
		if ((zip_last_lit & 7) == 0) {
		zip_flag_buf[zip_last_flags++] = zip_flags;
		zip_flags = 0;
		zip_flag_bit = 1;
		}
		if (zip_compr_level > 2 && (zip_last_lit & 4095) == 0) {
		var out_length = zip_last_lit * 8;
		var in_length = zip_strstart - zip_block_start;
		var dcode;
		for (dcode = 0; dcode < zip_D_CODES; dcode++) {
		out_length += zip_dyn_dtree[dcode].fc * (5 + zip_extra_dbits[dcode]);
		}
		out_length >>= 3;
		if (zip_last_dist < parseInt(zip_last_lit / 2) && out_length < parseInt(in_length / 2)) {
		return true;
		}
		}
		return zip_last_lit == zip_LIT_BUFSIZE - 1 || zip_last_dist == zip_DIST_BUFSIZE;
		};
		var zip_compress_block = function(ltree, dtree) {
		var dist;
		var lc;
		var lx = 0;
		var dx = 0;
		var fx = 0;
		var flag = 0;
		var code;
		var extra;
		if (zip_last_lit != 0) {
		do {
		if ((lx & 7) == 0) {
		flag = zip_flag_buf[fx++];
		}
		lc = zip_l_buf[lx++] & 255;
		if ((flag & 1) == 0) {
		zip_SEND_CODE(lc, ltree);
		} else {
		code = zip_length_code[lc];
		zip_SEND_CODE(code + zip_LITERALS + 1, ltree);
		extra = zip_extra_lbits[code];
		if (extra != 0) {
		lc -= zip_base_length[code];
		zip_send_bits(lc, extra);
		}
		dist = zip_d_buf[dx++];
		code = zip_D_CODE(dist);
		zip_SEND_CODE(code, dtree);
		extra = zip_extra_dbits[code];
		if (extra != 0) {
		dist -= zip_base_dist[code];
		zip_send_bits(dist, extra);
		}
		}
		flag >>= 1;
		} while (lx < zip_last_lit);
		}
		zip_SEND_CODE(zip_END_BLOCK, ltree);
		};
		var zip_Buf_size = 16;
		var zip_send_bits = function(value, length) {
		if (zip_bi_valid > zip_Buf_size - length) {
		zip_bi_buf |= value << zip_bi_valid;
		zip_put_short(zip_bi_buf);
		zip_bi_buf = value >> zip_Buf_size - zip_bi_valid;
		zip_bi_valid += length - zip_Buf_size;
		} else {
		zip_bi_buf |= value << zip_bi_valid;
		zip_bi_valid += length;
		}
		};
		var zip_bi_reverse = function(code, len) {
		var res = 0;
		do {
		res |= code & 1;
		code >>= 1;
		res <<= 1;
		} while (--len > 0);
		return res >> 1;
		};
		var zip_bi_windup = function() {
		if (zip_bi_valid > 8) {
		zip_put_short(zip_bi_buf);
		} else {
		if (zip_bi_valid > 0) {
		zip_put_byte(zip_bi_buf);
		}
		}
		zip_bi_buf = 0;
		zip_bi_valid = 0;
		};
		var zip_qoutbuf = function() {
		if (zip_outcnt != 0) {
		var q, i;
		q = zip_new_queue();
		if (zip_qhead == null) {
		zip_qhead = zip_qtail = q;
		} else {
		zip_qtail = zip_qtail.next = q;
		}
		q.len = zip_outcnt - zip_outoff;
		for (i = 0; i < q.len; i++) {
		q.ptr[i] = zip_outbuf[zip_outoff + i];
		}
		zip_outcnt = zip_outoff = 0;
		}
		};
		var zip_deflate = function(str, level) {
		var i, j;
		zip_deflate_data = str;
		zip_deflate_pos = 0;
		if (typeof level == "undefined") {
		level = zip_DEFAULT_LEVEL;
		}
		zip_deflate_start(level);
		var buff = new Array(1024);
		var aout = [];
		while ((i = zip_deflate_internal(buff, 0, buff.length)) > 0) {
		var cbuf = new Array(i);
		for (j = 0; j < i; j++) {
		cbuf[j] = String.fromCharCode(buff[j]);
		}
		aout[aout.length] = cbuf.join("");
		}
		zip_deflate_data = null;
		return aout.join("");
		};
		if (!ctx.RawDeflate) {
		ctx.RawDeflate = {};
		}
		ctx.RawDeflate.deflate = zip_deflate;
		})($wnd);
    }-*/;

    private static native void initRawInflate()/*-{
		(function(ctx) {
		var zip_WSIZE = 32768;
		var zip_STORED_BLOCK = 0;
		var zip_STATIC_TREES = 1;
		var zip_DYN_TREES = 2;
		var zip_lbits = 9;
		var zip_dbits = 6;
		var zip_INBUFSIZ = 32768;
		var zip_INBUF_EXTRA = 64;
		var zip_slide;
		var zip_wp;
		var zip_fixed_tl = null;
		var zip_fixed_td;
		var zip_fixed_bl, zip_fixed_bd;
		var zip_bit_buf;
		var zip_bit_len;
		var zip_method;
		var zip_eof;
		var zip_copy_leng;
		var zip_copy_dist;
		var zip_tl, zip_td;
		var zip_bl, zip_bd;
		var zip_inflate_data;
		var zip_inflate_pos;
		var zip_MASK_BITS = new Array(0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535);
		var zip_cplens = new Array(3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 0, 0);
		var zip_cplext = new Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0, 99, 99);
		var zip_cpdist = new Array(1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577);
		var zip_cpdext = new Array(0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13);
		var zip_border = new Array(16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15);
		var zip_HuftList = function() {
		this.next = null;
		this.list = null;
		};
		var zip_HuftNode = function() {
		this.e = 0;
		this.b = 0;
		this.n = 0;
		this.t = null;
		};
		var zip_HuftBuild = function(b, n, s, d, e, mm) {
		this.BMAX = 16;
		this.N_MAX = 288;
		this.status = 0;
		this.root = null;
		this.m = 0;
		var a;
		var c = new Array(this.BMAX + 1);
		var el;
		var f;
		var g;
		var h;
		var i;
		var j;
		var k;
		var lx = new Array(this.BMAX + 1);
		var p;
		var pidx;
		var q;
		var r = new zip_HuftNode;
		var u = new Array(this.BMAX);
		var v = new Array(this.N_MAX);
		var w;
		var x = new Array(this.BMAX + 1);
		var xp;
		var y;
		var z;
		var o;
		var tail;
		tail = this.root = null;
		for (i = 0; i < c.length; i++) {
		c[i] = 0;
		}
		for (i = 0; i < lx.length; i++) {
		lx[i] = 0;
		}
		for (i = 0; i < u.length; i++) {
		u[i] = null;
		}
		for (i = 0; i < v.length; i++) {
		v[i] = 0;
		}
		for (i = 0; i < x.length; i++) {
		x[i] = 0;
		}
		el = n > 256 ? b[256] : this.BMAX;
		p = b;
		pidx = 0;
		i = n;
		do {
		c[p[pidx]]++;
		pidx++;
		} while (--i > 0);
		if (c[0] == n) {
		this.root = null;
		this.m = 0;
		this.status = 0;
		return;
		}
		for (j = 1; j <= this.BMAX; j++) {
		if (c[j] != 0) {
		break;
		}
		}
		k = j;
		if (mm < j) {
		mm = j;
		}
		for (i = this.BMAX; i != 0; i--) {
		if (c[i] != 0) {
		break;
		}
		}
		g = i;
		if (mm > i) {
		mm = i;
		}
		for (y = 1 << j; j < i; j++, y <<= 1) {
		if ((y -= c[j]) < 0) {
		this.status = 2;
		this.m = mm;
		return;
		}
		}
		if ((y -= c[i]) < 0) {
		this.status = 2;
		this.m = mm;
		return;
		}
		c[i] += y;
		x[1] = j = 0;
		p = c;
		pidx = 1;
		xp = 2;
		while (--i > 0) {
		x[xp++] = j += p[pidx++];
		}
		p = b;
		pidx = 0;
		i = 0;
		do {
		if ((j = p[pidx++]) != 0) {
		v[x[j]++] = i;
		}
		} while (++i < n);
		n = x[g];
		x[0] = i = 0;
		p = v;
		pidx = 0;
		h = -1;
		w = lx[0] = 0;
		q = null;
		z = 0;
		for (; k <= g; k++) {
		a = c[k];
		while (a-- > 0) {
		while (k > w + lx[1 + h]) {
		w += lx[1 + h];
		h++;
		z = (z = g - w) > mm ? mm : z;
		if ((f = 1 << (j = k - w)) > a + 1) {
		f -= a + 1;
		xp = k;
		while (++j < z) {
		if ((f <<= 1) <= c[++xp]) {
		break;
		}
		f -= c[xp];
		}
		}
		if (w + j > el && w < el) {
		j = el - w;
		}
		z = 1 << j;
		lx[1 + h] = j;
		q = new Array(z);
		for (o = 0; o < z; o++) {
		q[o] = new zip_HuftNode;
		}
		if (tail == null) {
		tail = this.root = new zip_HuftList;
		} else {
		tail = tail.next = new zip_HuftList;
		}
		tail.next = null;
		tail.list = q;
		u[h] = q;
		if (h > 0) {
		x[h] = i;
		r.b = lx[h];
		r.e = 16 + j;
		r.t = q;
		j = (i & (1 << w) - 1) >> w - lx[h];
		u[h - 1][j].e = r.e;
		u[h - 1][j].b = r.b;
		u[h - 1][j].n = r.n;
		u[h - 1][j].t = r.t;
		}
		}
		r.b = k - w;
		if (pidx >= n) {
		r.e = 99;
		} else {
		if (p[pidx] < s) {
		r.e = p[pidx] < 256 ? 16 : 15;
		r.n = p[pidx++];
		} else {
		r.e = e[p[pidx] - s];
		r.n = d[p[pidx++] - s];
		}
		}
		f = 1 << k - w;
		for (j = i >> w; j < z; j += f) {
		q[j].e = r.e;
		q[j].b = r.b;
		q[j].n = r.n;
		q[j].t = r.t;
		}
		for (j = 1 << k - 1; (i & j) != 0; j >>= 1) {
		i ^= j;
		}
		i ^= j;
		while ((i & (1 << w) - 1) != x[h]) {
		w -= lx[h];
		h--;
		}
		}
		}
		this.m = lx[1];
		this.status = y != 0 && g != 1 ? 1 : 0;
		};
		var zip_GET_BYTE = function() {
		if (zip_inflate_data.length == zip_inflate_pos) {
		return -1;
		}
		return zip_inflate_data.charCodeAt(zip_inflate_pos++) & 255;
		};
		var zip_NEEDBITS = function(n) {
		while (zip_bit_len < n) {
		zip_bit_buf |= zip_GET_BYTE() << zip_bit_len;
		zip_bit_len += 8;
		}
		};
		var zip_GETBITS = function(n) {
		return zip_bit_buf & zip_MASK_BITS[n];
		};
		var zip_DUMPBITS = function(n) {
		zip_bit_buf >>= n;
		zip_bit_len -= n;
		};
		var zip_inflate_codes = function(buff, off, size) {
		var e;
		var t;
		var n;
		if (size == 0) {
		return 0;
		}
		n = 0;
		for (;;) {
		zip_NEEDBITS(zip_bl);
		t = zip_tl.list[zip_GETBITS(zip_bl)];
		e = t.e;
		while (e > 16) {
		if (e == 99) {
		return -1;
		}
		zip_DUMPBITS(t.b);
		e -= 16;
		zip_NEEDBITS(e);
		t = t.t[zip_GETBITS(e)];
		e = t.e;
		}
		zip_DUMPBITS(t.b);
		if (e == 16) {
		zip_wp &= zip_WSIZE - 1;
		buff[off + n++] = zip_slide[zip_wp++] = t.n;
		if (n == size) {
		return size;
		}
		continue;
		}
		if (e == 15) {
		break;
		}
		zip_NEEDBITS(e);
		zip_copy_leng = t.n + zip_GETBITS(e);
		zip_DUMPBITS(e);
		zip_NEEDBITS(zip_bd);
		t = zip_td.list[zip_GETBITS(zip_bd)];
		e = t.e;
		while (e > 16) {
		if (e == 99) {
		return -1;
		}
		zip_DUMPBITS(t.b);
		e -= 16;
		zip_NEEDBITS(e);
		t = t.t[zip_GETBITS(e)];
		e = t.e;
		}
		zip_DUMPBITS(t.b);
		zip_NEEDBITS(e);
		zip_copy_dist = zip_wp - t.n - zip_GETBITS(e);
		zip_DUMPBITS(e);
		while (zip_copy_leng > 0 && n < size) {
		zip_copy_leng--;
		zip_copy_dist &= zip_WSIZE - 1;
		zip_wp &= zip_WSIZE - 1;
		buff[off + n++] = zip_slide[zip_wp++] = zip_slide[zip_copy_dist++];
		}
		if (n == size) {
		return size;
		}
		}
		zip_method = -1;
		return n;
		};
		var zip_inflate_stored = function(buff, off, size) {
		var n;
		n = zip_bit_len & 7;
		zip_DUMPBITS(n);
		zip_NEEDBITS(16);
		n = zip_GETBITS(16);
		zip_DUMPBITS(16);
		zip_NEEDBITS(16);
		if (n != (~zip_bit_buf & 65535)) {
		return -1;
		}
		zip_DUMPBITS(16);
		zip_copy_leng = n;
		n = 0;
		while (zip_copy_leng > 0 && n < size) {
		zip_copy_leng--;
		zip_wp &= zip_WSIZE - 1;
		zip_NEEDBITS(8);
		buff[off + n++] = zip_slide[zip_wp++] = zip_GETBITS(8);
		zip_DUMPBITS(8);
		}
		if (zip_copy_leng == 0) {
		zip_method = -1;
		}
		return n;
		};
		var zip_inflate_fixed = function(buff, off, size) {
		if (zip_fixed_tl == null) {
		var i;
		var l = new Array(288);
		var h;
		for (i = 0; i < 144; i++) {
		l[i] = 8;
		}
		for (; i < 256; i++) {
		l[i] = 9;
		}
		for (; i < 280; i++) {
		l[i] = 7;
		}
		for (; i < 288; i++) {
		l[i] = 8;
		}
		zip_fixed_bl = 7;
		h = new zip_HuftBuild(l, 288, 257, zip_cplens, zip_cplext, zip_fixed_bl);
		if (h.status != 0) {
		alert("HufBuild error: " + h.status);
		return -1;
		}
		zip_fixed_tl = h.root;
		zip_fixed_bl = h.m;
		for (i = 0; i < 30; i++) {
		l[i] = 5;
		}
		zip_fixed_bd = 5;
		h = new zip_HuftBuild(l, 30, 0, zip_cpdist, zip_cpdext, zip_fixed_bd);
		if (h.status > 1) {
		zip_fixed_tl = null;
		alert("HufBuild error: " + h.status);
		return -1;
		}
		zip_fixed_td = h.root;
		zip_fixed_bd = h.m;
		}
		zip_tl = zip_fixed_tl;
		zip_td = zip_fixed_td;
		zip_bl = zip_fixed_bl;
		zip_bd = zip_fixed_bd;
		return zip_inflate_codes(buff, off, size);
		};
		var zip_inflate_dynamic = function(buff, off, size) {
		var i;
		var j;
		var l;
		var n;
		var t;
		var nb;
		var nl;
		var nd;
		var ll = new Array(286 + 30);
		var h;
		for (i = 0; i < ll.length; i++) {
		ll[i] = 0;
		}
		zip_NEEDBITS(5);
		nl = 257 + zip_GETBITS(5);
		zip_DUMPBITS(5);
		zip_NEEDBITS(5);
		nd = 1 + zip_GETBITS(5);
		zip_DUMPBITS(5);
		zip_NEEDBITS(4);
		nb = 4 + zip_GETBITS(4);
		zip_DUMPBITS(4);
		if (nl > 286 || nd > 30) {
		return -1;
		}
		for (j = 0; j < nb; j++) {
		zip_NEEDBITS(3);
		ll[zip_border[j]] = zip_GETBITS(3);
		zip_DUMPBITS(3);
		}
		for (; j < 19; j++) {
		ll[zip_border[j]] = 0;
		}
		zip_bl = 7;
		h = new zip_HuftBuild(ll, 19, 19, null, null, zip_bl);
		if (h.status != 0) {
		return -1;
		}
		zip_tl = h.root;
		zip_bl = h.m;
		n = nl + nd;
		i = l = 0;
		while (i < n) {
		zip_NEEDBITS(zip_bl);
		t = zip_tl.list[zip_GETBITS(zip_bl)];
		j = t.b;
		zip_DUMPBITS(j);
		j = t.n;
		if (j < 16) {
		ll[i++] = l = j;
		} else {
		if (j == 16) {
		zip_NEEDBITS(2);
		j = 3 + zip_GETBITS(2);
		zip_DUMPBITS(2);
		if (i + j > n) {
		return -1;
		}
		while (j-- > 0) {
		ll[i++] = l;
		}
		} else {
		if (j == 17) {
		zip_NEEDBITS(3);
		j = 3 + zip_GETBITS(3);
		zip_DUMPBITS(3);
		if (i + j > n) {
		return -1;
		}
		while (j-- > 0) {
		ll[i++] = 0;
		}
		l = 0;
		} else {
		zip_NEEDBITS(7);
		j = 11 + zip_GETBITS(7);
		zip_DUMPBITS(7);
		if (i + j > n) {
		return -1;
		}
		while (j-- > 0) {
		ll[i++] = 0;
		}
		l = 0;
		}
		}
		}
		}
		zip_bl = zip_lbits;
		h = new zip_HuftBuild(ll, nl, 257, zip_cplens, zip_cplext, zip_bl);
		if (zip_bl == 0) {
		h.status = 1;
		}
		if (h.status != 0) {
		if (h.status == 1) {
		}
		return -1;
		}
		zip_tl = h.root;
		zip_bl = h.m;
		for (i = 0; i < nd; i++) {
		ll[i] = ll[i + nl];
		}
		zip_bd = zip_dbits;
		h = new zip_HuftBuild(ll, nd, 0, zip_cpdist, zip_cpdext, zip_bd);
		zip_td = h.root;
		zip_bd = h.m;
		if (zip_bd == 0 && nl > 257) {
		return -1;
		}
		if (h.status == 1) {
		}
		if (h.status != 0) {
		return -1;
		}
		return zip_inflate_codes(buff, off, size);
		};
		var zip_inflate_start = function() {
		var i;
		if (zip_slide == null) {
		zip_slide = new Array(2 * zip_WSIZE);
		}
		zip_wp = 0;
		zip_bit_buf = 0;
		zip_bit_len = 0;
		zip_method = -1;
		zip_eof = false;
		zip_copy_leng = zip_copy_dist = 0;
		zip_tl = null;
		};
		var zip_inflate_internal = function(buff, off, size) {
		var n, i;
		n = 0;
		while (n < size) {
		if (zip_eof && zip_method == -1) {
		return n;
		}
		if (zip_copy_leng > 0) {
		if (zip_method != zip_STORED_BLOCK) {
		while (zip_copy_leng > 0 && n < size) {
		zip_copy_leng--;
		zip_copy_dist &= zip_WSIZE - 1;
		zip_wp &= zip_WSIZE - 1;
		buff[off + n++] = zip_slide[zip_wp++] = zip_slide[zip_copy_dist++];
		}
		} else {
		while (zip_copy_leng > 0 && n < size) {
		zip_copy_leng--;
		zip_wp &= zip_WSIZE - 1;
		zip_NEEDBITS(8);
		buff[off + n++] = zip_slide[zip_wp++] = zip_GETBITS(8);
		zip_DUMPBITS(8);
		}
		if (zip_copy_leng == 0) {
		zip_method = -1;
		}
		}
		if (n == size) {
		return n;
		}
		}
		if (zip_method == -1) {
		if (zip_eof) {
		break;
		}
		zip_NEEDBITS(1);
		if (zip_GETBITS(1) != 0) {
		zip_eof = true;
		}
		zip_DUMPBITS(1);
		zip_NEEDBITS(2);
		zip_method = zip_GETBITS(2);
		zip_DUMPBITS(2);
		zip_tl = null;
		zip_copy_leng = 0;
		}
		switch (zip_method) {
		case 0:
		i = zip_inflate_stored(buff, off + n, size - n);
		break;
		case 1:
		if (zip_tl != null) {
		i = zip_inflate_codes(buff, off + n, size - n);
		} else {
		i = zip_inflate_fixed(buff, off + n, size - n);
		}
		break;
		case 2:
		if (zip_tl != null) {
		i = zip_inflate_codes(buff, off + n, size - n);
		} else {
		i = zip_inflate_dynamic(buff, off + n, size - n);
		}
		break;
		default:
		i = -1;
		break;
		}
		if (i == -1) {
		if (zip_eof) {
		return 0;
		}
		return -1;
		}
		n += i;
		}
		return n;
		};
		var zip_inflate = function(str) {
		var i, j;
		zip_inflate_start();
		zip_inflate_data = str;
		zip_inflate_pos = 0;
		var buff = new Array(1024);
		var aout = [];
		while ((i = zip_inflate_internal(buff, 0, buff.length)) > 0) {
		var cbuf = new Array(i);
		for (j = 0; j < i; j++) {
		cbuf[j] = String.fromCharCode(buff[j]);
		}
		aout[aout.length] = cbuf.join("");
		}
		zip_inflate_data = null;
		return aout.join("");
		};
		if (!ctx.RawDeflate) {
		ctx.RawDeflate = {};
		}
		ctx.RawDeflate.inflate = zip_inflate;
		})($wnd);
    }-*/;

}
