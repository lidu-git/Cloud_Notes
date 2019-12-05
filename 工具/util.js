/**
 * 判断字符串是否为空
 * trim():去除字符串前后空格 
 */
function isEmpty(str) {
	if (str == null || str.trim() == "") {
		return true;
	}
	return false;
}