/*md5*/
if(typeof($jskey)!="object"){$jskey={};}
(function(){
var MD5=function(v){function RotateLeft(v,n){return(v<<n)|(v>>>(32-n));}function AddU(x,y){var X4,Y4,X8,Y8,z;X8=(x & 0x80000000);Y8=(y & 0x80000000);X4=(x & 0x40000000);Y4=(y & 0x40000000);z=(x & 0x3FFFFFFF)+(y & 0x3FFFFFFF);if(X4 & Y4){return(z ^ 0x80000000 ^ X8 ^ Y8);}if(X4 | Y4){if(z & 0x40000000){return(z ^ 0xC0000000 ^ X8 ^ Y8);}else{return(z ^ 0x40000000 ^ X8 ^ Y8);}}else{return(z ^ X8 ^ Y8);}}function F(x,y,z){return(x & y)|((~x)& z);}function G(x,y,z){return(x & z)|(y &(~z));}function H(x,y,z){return(x ^ y ^ z);}function I(x,y,z){return(y ^(x |(~z)));}function FF(a,b,c,d,x,s,ac){a=AddU(a,AddU(AddU(F(b,c,d),x),ac));return AddU(RotateLeft(a,s),b);};function GG(a,b,c,d,x,s,ac){a=AddU(a,AddU(AddU(G(b,c,d),x),ac));return AddU(RotateLeft(a,s),b);};function HH(a,b,c,d,x,s,ac){a=AddU(a,AddU(AddU(H(b,c,d),x),ac));return AddU(RotateLeft(a,s),b);};function II(a,b,c,d,x,s,ac){a=AddU(a,AddU(AddU(I(b,c,d),x),ac));return AddU(RotateLeft(a,s),b);};function ToWordArray(v){var c;var len=v.length;var _t1=len+8;var _t2=(_t1-(_t1 % 64))/64;var num=(_t2+1)*16;var d=Array(num-1);var bIndex=0;var bCount=0;while(bCount<len){c=(bCount-(bCount % 4))/4;bIndex=(bCount % 4)*8;d[c]=(d[c]|(v.charCodeAt(bCount)<<bIndex));bCount++;}c=(bCount-(bCount % 4))/4;bIndex=(bCount % 4)*8;d[c]=d[c]|(0x80<<bIndex);d[num-2]=len<<3;d[num-1]=len>>>29;return d;};function ToHex(v){var t="",tt="",lByte,i;for(i=0;i<=3;i++){lByte=(v>>>(i*8))& 255;tt="0"+lByte.toString(16);t=t+tt.substr(tt.length-2,2);}return t;};function Utf8Encode(v){v=v.replace(/\r\n/g,"\n");var s="";for(var n=0;n<v.length;n++){var c=v.charCodeAt(n);if(c<128){s+=String.fromCharCode(c);}else if((c>127)&&(c<2048)){s+=String.fromCharCode((c>>6)| 192);s+=String.fromCharCode((c & 63)| 128);}else{s+=String.fromCharCode((c>>12)| 224);s+=String.fromCharCode(((c>>6)& 63)| 128);s+=String.fromCharCode((c & 63)| 128);}}return s;};var x=[];var k,AA,BB,CC,DD,a,b,c,d;var S11=7,S12=12,S13=17,S14=22;var S21=5,S22=9,S23=14,S24=20;var S31=4,S32=11,S33=16,S34=23;var S41=6,S42=10,S43=15,S44=21;v=Utf8Encode(v);x=ToWordArray(v);a=0x67452301;b=0xEFCDAB89;c=0x98BADCFE;d=0x10325476;for(k=0;k<x.length;k+=16){AA=a;BB=b;CC=c;DD=d;a=FF(a,b,c,d,x[k+0],S11,0xD76AA478);d=FF(d,a,b,c,x[k+1],S12,0xE8C7B756);c=FF(c,d,a,b,x[k+2],S13,0x242070DB);b=FF(b,c,d,a,x[k+3],S14,0xC1BDCEEE);a=FF(a,b,c,d,x[k+4],S11,0xF57C0FAF);d=FF(d,a,b,c,x[k+5],S12,0x4787C62A);c=FF(c,d,a,b,x[k+6],S13,0xA8304613);b=FF(b,c,d,a,x[k+7],S14,0xFD469501);a=FF(a,b,c,d,x[k+8],S11,0x698098D8);d=FF(d,a,b,c,x[k+9],S12,0x8B44F7AF);c=FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);b=FF(b,c,d,a,x[k+11],S14,0x895CD7BE);a=FF(a,b,c,d,x[k+12],S11,0x6B901122);d=FF(d,a,b,c,x[k+13],S12,0xFD987193);c=FF(c,d,a,b,x[k+14],S13,0xA679438E);b=FF(b,c,d,a,x[k+15],S14,0x49B40821);a=GG(a,b,c,d,x[k+1],S21,0xF61E2562);d=GG(d,a,b,c,x[k+6],S22,0xC040B340);c=GG(c,d,a,b,x[k+11],S23,0x265E5A51);b=GG(b,c,d,a,x[k+0],S24,0xE9B6C7AA);a=GG(a,b,c,d,x[k+5],S21,0xD62F105D);d=GG(d,a,b,c,x[k+10],S22,0x2441453);c=GG(c,d,a,b,x[k+15],S23,0xD8A1E681);b=GG(b,c,d,a,x[k+4],S24,0xE7D3FBC8);a=GG(a,b,c,d,x[k+9],S21,0x21E1CDE6);d=GG(d,a,b,c,x[k+14],S22,0xC33707D6);c=GG(c,d,a,b,x[k+3],S23,0xF4D50D87);b=GG(b,c,d,a,x[k+8],S24,0x455A14ED);a=GG(a,b,c,d,x[k+13],S21,0xA9E3E905);d=GG(d,a,b,c,x[k+2],S22,0xFCEFA3F8);c=GG(c,d,a,b,x[k+7],S23,0x676F02D9);b=GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);a=HH(a,b,c,d,x[k+5],S31,0xFFFA3942);d=HH(d,a,b,c,x[k+8],S32,0x8771F681);c=HH(c,d,a,b,x[k+11],S33,0x6D9D6122);b=HH(b,c,d,a,x[k+14],S34,0xFDE5380C);a=HH(a,b,c,d,x[k+1],S31,0xA4BEEA44);d=HH(d,a,b,c,x[k+4],S32,0x4BDECFA9);c=HH(c,d,a,b,x[k+7],S33,0xF6BB4B60);b=HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);a=HH(a,b,c,d,x[k+13],S31,0x289B7EC6);d=HH(d,a,b,c,x[k+0],S32,0xEAA127FA);c=HH(c,d,a,b,x[k+3],S33,0xD4EF3085);b=HH(b,c,d,a,x[k+6],S34,0x4881D05);a=HH(a,b,c,d,x[k+9],S31,0xD9D4D039);d=HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);c=HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);b=HH(b,c,d,a,x[k+2],S34,0xC4AC5665);a=II(a,b,c,d,x[k+0],S41,0xF4292244);d=II(d,a,b,c,x[k+7],S42,0x432AFF97);c=II(c,d,a,b,x[k+14],S43,0xAB9423A7);b=II(b,c,d,a,x[k+5],S44,0xFC93A039);a=II(a,b,c,d,x[k+12],S41,0x655B59C3);d=II(d,a,b,c,x[k+3],S42,0x8F0CCC92);c=II(c,d,a,b,x[k+10],S43,0xFFEFF47D);b=II(b,c,d,a,x[k+1],S44,0x85845DD1);a=II(a,b,c,d,x[k+8],S41,0x6FA87E4F);d=II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);c=II(c,d,a,b,x[k+6],S43,0xA3014314);b=II(b,c,d,a,x[k+13],S44,0x4E0811A1);a=II(a,b,c,d,x[k+4],S41,0xF7537E82);d=II(d,a,b,c,x[k+11],S42,0xBD3AF235);c=II(c,d,a,b,x[k+2],S43,0x2AD7D2BB);b=II(b,c,d,a,x[k+9],S44,0xEB86D391);a=AddU(a,AA);b=AddU(b,BB);c=AddU(c,CC);d=AddU(d,DD);}v=(ToHex(a)+ToHex(b)+ToHex(c)+ToHex(d)).toUpperCase();return v;};
$jskey.md5=MD5;
})();

/*des*/
if(typeof($jskey)!="object"){$jskey={};}
(function(){
var getKeyBytes=function(key){var keyBytes=[];var len=key.length;var iterator=parseInt(len / 4);var remainder=len % 4;var i=0;for(i=0;i<iterator;i++){keyBytes[i]=strToBt(key.substring(i * 4+0,i * 4+4));}if(remainder>0){keyBytes[i]=strToBt(key.substring(i * 4+0,len));}return keyBytes;};var strToBt=function(str){var len=str.length;var bt=new Array(64);var i=0,j=0,p=0,q=0;for(i=0;i<len&&i<4;i++){var k=str.charCodeAt(i);for(j=0;j<16;j++){var pow=1,m=0;for(m=15;m>j;m--){pow *=2;}bt[16 * i+j]=parseInt(k / pow)% 2;}}for(p=len;p<4;p++){var k=0;for(q=0;q<16;q++){var pow=1,m=0;for(m=15;m>q;m--){pow *=2;}bt[16 * p+q]=parseInt(k / pow)% 2;}}return bt;};var binaryArray=["0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"];var bt4ToHex=function(binary){var hex="";try{var i=parseInt(binary,2);if(i>-1&&i<16){hex=i.toString(16);}}catch(ex){}return hex.toUpperCase();;};var getBoxBinary=function(i){var binary="";if(i>-1&&i<16){binary=binaryArray[i];}return binary;};var hexToBt4=function(hex){var binary="";try{var i=parseInt(hex,16);if(i>-1&&i<16){binary=binaryArray[i];}}catch(ex){}return binary;};var byteToString=function(byteData){var str="";for(var i=0;i<4;i++){var count=0;for(var j=0;j<16;j++){var pow=1;for(var m=15;m>j;m--){pow *=2;}count+=byteData[16 * i+j]* pow;}if(count !=0){str+=String.fromCharCode(count);}}return str;};var bt64ToHex=function(byteData){var hex="";for(var i=0;i<16;i++){var bt="";for(var j=0;j<4;j++){bt+=byteData[i * 4+j];}hex+=bt4ToHex(bt);}return hex;};var hexToBt64=function(hex){var binary="";for(var i=0;i<16;i++){binary+=hexToBt4(hex.substring(i,i+1));}return binary;};var enc=function(dataByte,keyByte){var keys=generateKeys(keyByte);var ipByte=initPermute(dataByte);var ipLeft=new Array(32);var ipRight=new Array(32);var tempLeft=new Array(32);var i=0,j=0,k=0,m=0,n=0;for(k=0;k<32;k++){ipLeft[k]=ipByte[k];ipRight[k]=ipByte[32+k];}for(i=0;i<16;i++){for(j=0;j<32;j++){tempLeft[j]=ipLeft[j];ipLeft[j]=ipRight[j];}var key=new Array(48);for(m=0;m<48;m++){key[m]=keys[i][m];}var tempRight=xor(pPermute(sBoxPermute(xor(expandPermute(ipRight),key))),tempLeft);for(n=0;n<32;n++){ipRight[n]=tempRight[n];}}var finalData=new Array(64);for(i=0;i<32;i++){finalData[i]=ipRight[i];finalData[32+i]=ipLeft[i];}return finallyPermute(finalData);};var dec=function(dataByte,keyByte){var keys=generateKeys(keyByte);var ipByte=initPermute(dataByte);var ipLeft=new Array(32);var ipRight=new Array(32);var tempLeft=new Array(32);var i=0,j=0,k=0,m=0,n=0;for(k=0;k<32;k++){ipLeft[k]=ipByte[k];ipRight[k]=ipByte[32+k];}for(i=15;i>=0;i--){for(j=0;j<32;j++){tempLeft[j]=ipLeft[j];ipLeft[j]=ipRight[j];}var key=new Array(48);for(m=0;m<48;m++){key[m]=keys[i][m];}var tempRight=xor(pPermute(sBoxPermute(xor(expandPermute(ipRight),key))),tempLeft);for(n=0;n<32;n++){ipRight[n]=tempRight[n];}}var finalData=new Array(64);for(i=0;i<32;i++){finalData[i]=ipRight[i];finalData[32+i]=ipLeft[i];}return finallyPermute(finalData);};var initPermute=function(originalData){var ipByte=new Array(64);var i=0,m=1,n=0,j,k;for(i=0,m=1,n=0;i<4;i++,m+=2,n+=2){for(j=7,k=0;j>=0;j--,k++){ipByte[i * 8+k]=originalData[j * 8+m];ipByte[i * 8+k+32]=originalData[j * 8+n];}}return ipByte;};var expandPermute=function(rightData){var epByte=new Array(48);for(var i=0;i<8;i++){if(i==0){epByte[i * 6+0]=rightData[31];}else{epByte[i * 6+0]=rightData[i * 4-1];}epByte[i * 6+1]=rightData[i * 4+0];epByte[i * 6+2]=rightData[i * 4+1];epByte[i * 6+3]=rightData[i * 4+2];epByte[i * 6+4]=rightData[i * 4+3];if(i==7){epByte[i * 6+5]=rightData[0];}else{epByte[i * 6+5]=rightData[i * 4+4];}}return epByte;};var xor=function(byteOne,byteTwo){var xorByte=new Array(byteOne.length);for(var i=0;i<byteOne.length;i++){xorByte[i]=byteOne[i]^ byteTwo[i];}return xorByte;};var sBoxPermute=function(expandByte){var sBoxByte=new Array(32);var binary="";var s1=[[14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7],[0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8],[4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0],[15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13]];var s2=[[15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10],[3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5],[0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15],[13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9]];var s3=[[10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8],[13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1],[13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7],[1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12]];var s4=[[7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15],[13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9],[10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4],[3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14]];var s5=[[2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9],[14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6],[4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14],[11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3]];var s6=[[12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11],[10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8],[9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6],[4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13]];var s7=[[4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1],[13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6],[1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2],[6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12]];var s8=[[13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7],[1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2],[7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8],[2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11]];for(var m=0;m<8;m++){var i=0,j=0;i=expandByte[m * 6+0]* 2+expandByte[m * 6+5];j=expandByte[m * 6+1]* 2 * 2 * 2+expandByte[m * 6+2]* 2 * 2+expandByte[m * 6+3]* 2+expandByte[m * 6+4];switch(m){case 0:binary=getBoxBinary(s1[i][j]);break;case 1:binary=getBoxBinary(s2[i][j]);break;case 2:binary=getBoxBinary(s3[i][j]);break;case 3:binary=getBoxBinary(s4[i][j]);break;case 4:binary=getBoxBinary(s5[i][j]);break;case 5:binary=getBoxBinary(s6[i][j]);break;case 6:binary=getBoxBinary(s7[i][j]);break;case 7:binary=getBoxBinary(s8[i][j]);break;}sBoxByte[m * 4+0]=parseInt(binary.substring(0,1));sBoxByte[m * 4+1]=parseInt(binary.substring(1,2));sBoxByte[m * 4+2]=parseInt(binary.substring(2,3));sBoxByte[m * 4+3]=parseInt(binary.substring(3,4));}return sBoxByte;};var pPermute=function(e){var p=[e[15],e[6],e[19],e[20],e[28],e[11],e[27],e[16],e[0],e[14],e[22],e[25],e[4],e[17],e[30],e[9],e[1],e[7],e[23],e[13],e[31],e[26],e[2],e[8],e[18],e[12],e[29],e[5],e[21],e[10],e[3],e[24]];return p;};var finallyPermute=function(e){var p=[e[39],e[7],e[47],e[15],e[55],e[23],e[63],e[31],e[38],e[6],e[46],e[14],e[54],e[22],e[62],e[30],e[37],e[5],e[45],e[13],e[53],e[21],e[61],e[29],e[36],e[4],e[44],e[12],e[52],e[20],e[60],e[28],e[35],e[3],e[43],e[11],e[51],e[19],e[59],e[27],e[34],e[2],e[42],e[10],e[50],e[18],e[58],e[26],e[33],e[1],e[41],e[9],e[49],e[17],e[57],e[25],e[32],e[0],e[40],e[8],e[48],e[16],e[56],e[24]];return p;};var generateKeys=function(keyByte){var e=new Array(56);var keys=[[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[]];var loop=[1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1];for(var i=0;i<7;i++){for(var j=0,k=7;j<8;j++,k--){e[i * 8+j]=keyByte[8 * k+i];}}for(var i=0;i<16;i++){var tempLeft=0;var tempRight=0;for(var j=0;j<loop[i];j++){tempLeft=e[0];tempRight=e[28];for(k=0;k<27;k++){e[k]=e[k+1];e[28+k]=e[29+k];}e[27]=tempLeft;e[55]=tempRight;}var t=[e[13],e[16],e[10],e[23],e[0],e[4],e[2],e[27],e[14],e[5],e[20],e[9],e[22],e[18],e[11],e[3],e[25],e[7],e[15],e[6],e[26],e[19],e[12],e[1],e[40],e[51],e[30],e[36],e[46],e[54],e[29],e[39],e[50],e[44],e[32],e[47],e[43],e[48],e[38],e[55],e[33],e[52],e[45],e[41],e[49],e[35],e[28],e[31]];for(var m=0;m<48;m++){keys[i][m]=t[m];}}return keys;};
var encodeDes=function(str,key){var len=str.length;var encData="";var keyBt;var length=0;if(key !=null&&key !=""){keyBt=getKeyBytes(key);length=keyBt.length;var iterator=parseInt(len / 4);var remainder=len % 4;for(var i=0;i<iterator;i++){var tempData=str.substring(i * 4+0,i * 4+4);var tempByte=strToBt(tempData);var encByte;var tempBt=tempByte;for(var x=0;x<length;x++){tempBt=enc(tempBt,keyBt[x]);}encByte=tempBt;encData+=bt64ToHex(encByte);}if(remainder>0){var remainderData=str.substring(iterator * 4+0,len);var tempByte=strToBt(remainderData);var encByte;var tempBt=tempByte;for(var x=0;x<length;x++){tempBt=enc(tempBt,keyBt[x]);}encByte=tempBt;encData+=bt64ToHex(encByte);}}return encData;};
var decodeDes=function(str,key){var len=str.length;var decStr="";var keyBt;var length=0;if(key !=null&&key !=""){keyBt=getKeyBytes(key);length=keyBt.length;var iterator=parseInt(len / 16);var i=0;for(i=0;i<iterator;i++){var tempData=str.substring(i * 16+0,i * 16+16);var strByte=hexToBt64(tempData);var intByte=new Array(64);for(var j=0;j<64;j++){intByte[j]=parseInt(strByte.substring(j,j+1));}var decByte;var tempBt=intByte;for(var x=length-1;x>=0;x--){tempBt=dec(tempBt,keyBt[x]);}decByte=tempBt;decStr+=byteToString(decByte);}}return decStr;};
$jskey.encodeDes=encodeDes;
$jskey.decodeDes=decodeDes;
})();

/*sso*/
function _$(id){return document.getElementById(id);}
var x_new = "\u65b0";// 新
var x_old = "\u539f";// 原

var x_null = "\u4e0d\u80fd\u4e3a\u7a7a";
var x_noacc = "\u8d26\u53f7"+x_null;// 账号不能为空
var x_nopwd = "\u5bc6\u7801"+x_null;// 密码不能为空
var x_nocode = "\u9a8c\u8bc1\u7801"+x_null;// 验证码不能为空
var x_noeq = "\u786e\u8ba4\u5bc6\u7801\u4e0d\u4e00\u81f4";// 确认密码不一致
function _uncheck(id){
	var s = "";
	if(_$(id)){
		if(id == "account"){if(!_$(id).value){s=x_noacc;}}// 账号不能为空
		if(id == "oldpassword"){if(!_$(id).value){s=x_old+x_nopwd;}}//原密码不能为空
		if(id == "password"){
			if(_$(id+'2')){
				if((/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(_$(id).value))){
					s=(_$("oldpassword")?x_new:"")+"\u5bc6\u7801\u4e0d\u5c11\u4e8e\u0036\u4f4d\uff0c\u4e0d\u80fd\u7eaf\u6570\u5b57";// 新   密码不少于6位，不能纯数字
				}else{
					if(_$(id+"2")&&_$(id).value!=_$(id+"2").value){s = x_noeq;}// 确认密码不一致
				}
			}
			else{
				if(id == "password"){if(!_$(id).value){s=(_$("oldpassword")?x_new:"")+x_nopwd;}else{if(_$(id+"2")&&_$(id).value!=_$(id+"2").value){s = x_noeq;}}}
			}
		}
		if(id == "password2"){if(_$(id).value!=_$("password").value){s = x_noeq;}}
		if(id == "authcode"){if(!_$(id).value){s=x_nocode;}}
		if(s != "" && _$("msgdiv")){
			s = "<i>&#xf1010;</i> "+s+"<br>";
			_$("msg").innerHTML = s;
			_$("msgdiv").style.display = "block";
			return true;
		}
		else{
			_$("msgdiv").style.display= "none";
		}
	}
	return false;
}
function registEvent($e, et, fn){$e.attachEvent ? $e.attachEvent("on"+et, fn) : $e.addEventListener(et, fn, false);}
function registKey(id){
	try{
		registEvent(_$(id), "keydown", function(event){if(event.keyCode == 13){doclick();}});
		registEvent(_$(id), "keyup", function(event){_uncheck(this.getAttribute("id"));});
	}
	catch(e){
	}
}
registKey("account");
registKey("oldpassword");
registKey("password");
registKey("password2");
registKey("authcode");

var dd = document, cc = "coo" + "kie";
function setCoo(k,v,d){var x=new Date();x.setDate(x.getDate()+d);dd[cc]=k+"="+escape(v)+((d==null)?"":";expires="+x.toGMTString());}
function getCoo(k){if(dd[cc].length>0){var x1=dd[cc].indexOf(k+"=");if(x1!=-1){x1=x1+k.length+1;x2=dd[cc].indexOf(";",x1);if(x2==-1){x2=dd[cc].length;}return unescape(dd[cc].substring(x1,x2));}}return "";}
if(_$("savename")){
	var _x = getCoo("savename");
	if(_x.length > 0){_$("account").value = _x;_$("savename").checked = true;}else {_$("account").value = "";_$("savename").checked = false;}
}
function docheck(){
	if(_$("savename")){
		if(_$("savename").checked){setCoo("savename",_$("account").value,365);}
		else{setCoo("savename","",0);}
	}
	return (_uncheck("account") || _uncheck("oldpassword") || _uncheck("password") || _uncheck("password2") || _uncheck("authcode"));
}
if(_$("oldpassword")){_$("oldpassword").value = "";}
if(_$("password")){_$("password").value = "";}
if(_$("password2")){_$("password2").value = "";}
if(_$("mycode")){_$("mycode").click();}
_$((_$("account").value == "")?"account":"password").focus();

