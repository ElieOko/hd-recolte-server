<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AgentController;
use App\Http\Controllers\ClientController;
use App\Http\Controllers\ClientTrashController;

Route::post('client/auth', [ClientController::class, 'auth_client']);
Route::post('agent/auth', [AgentController::class, 'auth_agent']);
Route::post('trash/clean/{id}', [ClientTrashController::class, 'clean']);
Route::resource('agent', AgentController::class)->only([ 'index','store']);
Route::resource('client', ClientController::class)->only(['store','index']);
Route::resource('trash', ClientTrashController::class)->only(['index', 'store']);
Route::get('trash/{id}', [ClientTrashController::class,'show']);
// Route::resource('state_trash', ClientController::class)->only(['index', 'store']);

Route::middleware(['auth:sanctum'])->group(function () { 
    // Route::resource('client', ClientController::class)->only(['index', 'store']);
});
