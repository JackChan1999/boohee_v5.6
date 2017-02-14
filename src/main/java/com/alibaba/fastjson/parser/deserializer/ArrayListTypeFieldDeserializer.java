package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {
    private ObjectDeserializer deserializer;
    private int itemFastMatchToken;
    private final Type itemType;

    public ArrayListTypeFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);
        if (getFieldType() instanceof ParameterizedType) {
            this.itemType = ((ParameterizedType) getFieldType()).getActualTypeArguments()[0];
        } else {
            this.itemType = Object.class;
        }
    }

    public int getFastMatchToken() {
        return 14;
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (parser.getLexer().token() == 8) {
            setValue(object, null);
            return;
        }
        ArrayList list = new ArrayList();
        ParseContext context = parser.getContext();
        parser.setContext(context, object, this.fieldInfo.getName());
        parseArray(parser, objectType, list);
        parser.setContext(context);
        if (object == null) {
            fieldValues.put(this.fieldInfo.getName(), list);
        } else {
            setValue(object, (Object) list);
        }
    }

    public final void parseArray(DefaultJSONParser parser, Type objectType, Collection array) {
        int i;
        Type itemType = this.itemType;
        ObjectDeserializer itemTypeDeser = this.deserializer;
        if ((itemType instanceof TypeVariable) && (objectType instanceof ParameterizedType)) {
            TypeVariable typeVar = (TypeVariable) itemType;
            ParameterizedType paramType = (ParameterizedType) objectType;
            Class<?> objectClass = null;
            if (paramType.getRawType() instanceof Class) {
                objectClass = (Class) paramType.getRawType();
            }
            int paramIndex = -1;
            if (objectClass != null) {
                int size = objectClass.getTypeParameters().length;
                for (i = 0; i < size; i++) {
                    if (objectClass.getTypeParameters()[i].getName().equals(typeVar.getName())) {
                        paramIndex = i;
                        break;
                    }
                }
            }
            if (paramIndex != -1) {
                itemType = paramType.getActualTypeArguments()[paramIndex];
                if (!itemType.equals(this.itemType)) {
                    itemTypeDeser = parser.getConfig().getDeserializer(itemType);
                }
            }
        }
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() != 14) {
            String errorMessage = "exepct '[', but " + JSONToken.name(lexer.token());
            if (objectType != null) {
                errorMessage = errorMessage + ", type : " + objectType;
            }
            throw new JSONException(errorMessage);
        }
        if (itemTypeDeser == null) {
            itemTypeDeser = parser.getConfig().getDeserializer(itemType);
            this.deserializer = itemTypeDeser;
            this.itemFastMatchToken = this.deserializer.getFastMatchToken();
        }
        lexer.nextToken(this.itemFastMatchToken);
        i = 0;
        while (true) {
            if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer.token() == 16) {
                    lexer.nextToken();
                }
            }
            if (lexer.token() == 15) {
                lexer.nextToken(16);
                return;
            }
            array.add(itemTypeDeser.deserialze(parser, itemType, Integer.valueOf(i)));
            parser.checkListResolve(array);
            if (lexer.token() == 16) {
                lexer.nextToken(this.itemFastMatchToken);
            }
            i++;
        }
    }
}
