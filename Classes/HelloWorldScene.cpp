#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

USING_NS_CC;

Scene* HelloWorld::createScene()
{
    return HelloWorld::create();
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !Scene::init() )
    {
        return false;
    }
    
    auto visibleSize = Director::getInstance()->getVisibleSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();

    /////////////////////////////
    // 2. add a menu item with "X" image, which is clicked to quit the program
    //    you may modify it.

    // add a "close" icon to exit the progress. it's an autorelease object
    auto closeItem = MenuItemImage::create(
                                           "CloseNormal.png",
                                           "CloseSelected.png",
                                           CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
    
    closeItem->setPosition(Vec2(origin.x + visibleSize.width - closeItem->getContentSize().width/2 ,
                                origin.y + closeItem->getContentSize().height/2));

    // create menu, it's an autorelease object
    auto menu = Menu::create(closeItem, NULL);
    menu->setPosition(Vec2::ZERO);
    this->addChild(menu, 1);

    /////////////////////////////
    // 3. add your codes below...

    // add a label shows "Hello World"
    // create and initialize a label
    
    auto label = Label::createWithTTF("Hello World", "fonts/Marker Felt.ttf", 24);
    
    // position the label on the center of the screen
    label->setPosition(Vec2(origin.x + visibleSize.width/2,
                            origin.y + visibleSize.height - label->getContentSize().height));

    // add the label as a child to this layer
    this->addChild(label, 1);

//    // add "HelloWorld" splash screen"
//    auto sprite = Sprite::create("HelloWorld.png");
//
//    // position the sprite on the center of the screen
//    sprite->setPosition(Vec2(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));
//
//    // add the sprite as a child to this layer
//    this->addChild(sprite, 0);
    
    _monsters = Array::create();
    _fireballs = Array::create();
    
    _monsters->retain();
    _fireballs->retain();
    
    auto background = Sprite::create("background.jpeg");
    background->setPosition(Vec2(visibleSize.width/2,visibleSize.height/2));
    background->setContentSize(Size(visibleSize.width,visibleSize.height));
    this->addChild(background);
    
    _player = Sprite::create("player.png");
    _player->setPosition(Vec2(visibleSize.width * 0.1, visibleSize.height * 0.5));
    _player->setScale(0.05, 0.05);
    this->addChild(_player);
    
    srand((unsigned int)time(nullptr));
    this->schedule(schedule_selector(HelloWorld::addMonster), 1.5);
    
    auto eventListener = EventListenerTouchOneByOne::create();
    eventListener->onTouchBegan = CC_CALLBACK_2(HelloWorld::onTouchBegan, this);
    this->getEventDispatcher()->addEventListenerWithSceneGraphPriority(eventListener, _player);
    
    auto keyListener = EventListenerKeyboard::create();
    keyListener->onKeyPressed = [=](EventKeyboard::KeyCode keyCode, Event* event){
        keys[keyCode] = true;
    };
    keyListener->onKeyReleased = [=](EventKeyboard::KeyCode keyCode, Event* event){
        keys[keyCode] = false;
    };
    
    this->getEventDispatcher()->addEventListenerWithSceneGraphPriority(keyListener, _player);
    
    this->schedule(schedule_selector(HelloWorld::update));
    
    return true;
}


void HelloWorld::menuCloseCallback(Ref* pSender)
{
    //Close the cocos2d-x game scene and quit the application
    Director::getInstance()->end();

    #if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    exit(0);
#endif
    
    /*To navigate back to native iOS screen(if present) without quitting the application  ,do not use Director::getInstance()->end() and exit(0) as given above,instead trigger a custom event created in RootViewController.mm as below*/
    
    //EventCustom customEndEvent("game_scene_close_event");
    //_eventDispatcher->dispatchEvent(&customEndEvent);
    
    
}

void HelloWorld::addMonster(float dt) {
    auto monster = Sprite::create("monster.png");
    
    // 1
    monster->setScale(0.1, 0.1);
    auto monsterContentSize = monster->getContentSize();
    auto selfContentSize = this->getContentSize();
    int minY = monsterContentSize.height/2*0.1;
    int maxY = selfContentSize.height - monsterContentSize.height/2*0.1;
    int rangeY = maxY - minY;
    int randomY = (rand() % rangeY) + minY;

    monster->setPosition(Vec2(selfContentSize.width + monsterContentSize.width/2, randomY));
    monster->setTag(1);
    
    this->addChild(monster);
    _monsters->addObject(monster);

    // 2
    int minDuration = 5.0;
    int maxDuration = 10.0;
    int rangeDuration = maxDuration - minDuration;
    int randomDuration = (rand() % rangeDuration) + minDuration;

    // 3
    auto actionMove = MoveTo::create(randomDuration, Vec2(-monsterContentSize.width/2, randomY));
    auto actionRemove = CallFuncN::create(this,callfuncN_selector(HelloWorld::moveFinish));
    monster->runAction(Sequence::create(actionMove,actionRemove, nullptr));
}

void HelloWorld::moveFinish(Node* sender){
    Sprite* toDelete = (Sprite*) sender;
    //tag1为monster，2为fireball
    if(toDelete->getTag()==1){
        _monsters->removeObject(toDelete);
    }else if(toDelete->getTag()==2){
        _fireballs->removeObject(toDelete);
    }
}

bool HelloWorld::onTouchBegan(Touch *touch, Event *unused_event) {
    // 1  - Just an example for how to get the  _player object
    //auto node = unused_event->getCurrentTarget();
    
    // 2
    Vec2 touchLocation = touch->getLocation();
    Vec2 offset = touchLocation - _player->getPosition();
    
    // 4
    auto fireball = Sprite::create("fireball.png");
    fireball->setPosition(_player->getPosition());
    fireball->setTag(2);
    this->addChild(fireball);
    
    _fireballs->addObject(fireball);
    
    // 5
    offset.normalize();
    auto shootAmount = offset * 1000;
    
    // 6
    auto realDest = shootAmount + fireball->getPosition();
    
    // 7
    auto actionMove = MoveTo::create(2.0f, realDest);
    auto actionRemove = RemoveSelf::create();
    fireball->runAction(Sequence::create(actionMove,actionRemove,     nullptr));
    
    return true;
}

void HelloWorld::update(float dt){
    int offsetX = 0, offsetY = 0;
    auto w = EventKeyboard::KeyCode::KEY_W;
    auto a = EventKeyboard::KeyCode::KEY_A;
    auto s = EventKeyboard::KeyCode::KEY_S;
    auto d = EventKeyboard::KeyCode::KEY_D;
    if(keys[w]){
        offsetY = OFFSET;
    }
    if(keys[a]){
        offsetX = -OFFSET;
    }
    if(keys[s]){
        offsetY = -OFFSET;
    }
    if(keys[d]){
        offsetX = OFFSET;
    }
    auto moveTo = MoveTo::create(0.3, Vec2(_player->getPositionX() + offsetX, _player->getPositionY() + offsetY));
    _player->runAction(moveTo);
    
    for(int i=0;i<_monsters->count();i++){
        Sprite* monster = (Sprite*)_monsters->getObjectAtIndex(i);
        for(int j=0;j<_fireballs->count();j++){
            Sprite* fireball = (Sprite*) _fireballs->getObjectAtIndex(j);
            if(monster->getBoundingBox().intersectsRect(fireball->getBoundingBox())){
                _monsters->removeObject(monster);
                _fireballs->removeObject(fireball);
                this->removeChild(monster);
                this->removeChild(fireball);
            }
        }
    }
}

