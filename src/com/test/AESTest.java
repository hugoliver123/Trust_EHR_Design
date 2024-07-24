package com.test;

import com.util.AES;

public class AESTest {
    public static void main(String[] args) {
        String cipher = "HZCI8MC8PY8s0GPHHeJuGK2l9ceo47NmeSDj4FhkOOM/nYn2l0QDMcw+9lMgHz80dYvhuGgy/OF1V1GCYEzwg0z0VAnZsLxl8VD2dXsbHiRP9EjbE45fUQvlmVKEEtLqfJXekC76+GLoKhvhxq3LaQ==";
        String key = "D56FB6038594FE372E4C6849DFB6AC2A";
        System.out.println(AES.aes_decrypt(cipher, key));
    }
}
