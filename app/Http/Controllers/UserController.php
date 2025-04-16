<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class UserController extends Controller
{
    //
    // public function login(Request $request) {
    //     $validator = Validator::make($request->all(),[
    //         "username" =>'required|string',
    //         "password" =>'required|string'
    //     ]);
    //     if($validator->stopOnFirstFailure()->fails()){
    //         return response()->json([
    //             'message' => $validator->errors()
    //          ],402);
    //     }
    //     $field = $validator->validated();
    //     $user = User::where('username',$field['username'])->first() ;
    //     if(!$user){
    //         return response()->json([
    //            'message' => 'Utilisateur non valide'
    //         ],404);
    //     }
    //     if($field['password'] != $user->password){
    //         return response()->json([
    //            'message' => 'Mot de passe incorrecte'
    //         ],404);
    //     }
       
    // }

    // public function register(Request $request) {
    //     $data["sys"] ="User System";
    //     $account = "User Sytem";
    //     $validator = Validator::make($request->all(),[
    //         'password' => 'string',
    //         'email'=>'string',
    //     ]);

    //     if($validator->stopOnFirstFailure()->fails()){
    //         return response()->json([
    //             'message' => $validator->errors(),
    //          ],403);
    //     }
    //     // $field = $validator->validated();
       
    //     //     $token = $user->createToken('token')->plainTextToken;
    // }
}
