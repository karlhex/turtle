const fs = require('fs');
const path = require('path');

const typesDir = path.join(__dirname, '../../src/main/java/com/fwai/turtle/types');

function processEnumFile(filePath) {
  let content = fs.readFileSync(filePath, 'utf8');
  // 检查是否已包含 description 字段
  if (content.includes('private final String description')) {
    console.log(path.basename(filePath), '已处理，跳过');
    return;
  }

  // 1. 枚举项处理：ONBOARDING, //入职培训 => ONBOARDING("入职培训"),
  content = content.replace(
    /([A-Z0-9_]+)\s*(,|;)\s*\/\/\s*([^\n\r]*)/g,
    '$1("$3")$2'
  );
  // 没有注释的项补空字符串
  content = content.replace(
    /([A-Z0-9_]+)\s*(,|;)(?!\s*")/g,
    '$1("")$2'
  );

  // 2. 在第一个 { 后插入字段和构造方法
  content = content.replace(
    /(\{)/,
    `$1\n    private final String description;\n\n    ${getEnumName(filePath)}(String description) {\n        this.description = description;\n    }\n`
  );

  // 3. 在最后一个 } 前插入 getDescription 和 get 方法
  content = content.replace(
    /(\}\s*)$/,
    `
    public String getDescription() {
        return description;
    }

    public static ${getEnumName(filePath)} get(String name) {
        for (${getEnumName(filePath)} e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
$1`
  );

  fs.writeFileSync(filePath, content, 'utf8');
  console.log(path.basename(filePath), '处理完成');
}

function getEnumName(filePath) {
  const match = fs.readFileSync(filePath, 'utf8').match(/enum\s+(\w+)/);
  return match ? match[1] : 'UNKNOWN';
}

fs.readdirSync(typesDir).forEach(file => {
  if (file.endsWith('.java')) {
    processEnumFile(path.join(typesDir, file));
  }
});
