// JavaScript Document
//常用的、有用的函数

//让对象布置在页面中间
function mediate(obj){
	obj.css('margin-left',($(window).width()-parseInt(obj.css('width')))/2);
};
//使对象达到屏幕最大的宽度
function maxWidth(obj){
	obj.css('width',$(window).width());
};